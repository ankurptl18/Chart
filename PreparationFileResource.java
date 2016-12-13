package com.inetpsa.poc00.rest.preparationfile;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.seedstack.jpa.JpaUnit;
import org.seedstack.seed.transaction.Transactional;

import com.inetpsa.poc00.Config;
import com.inetpsa.poc00.common.PropertiesLang;
import com.inetpsa.poc00.domain.genericpreparationchecklist.GenericPreparationChecklist;
import com.inetpsa.poc00.domain.genericpreparationitem.GenericPreparationItem;
import com.inetpsa.poc00.domain.preparationchecklist.PreparationCheckList;
import com.inetpsa.poc00.domain.preparationchecklist.PreparationCheckListFactory;
import com.inetpsa.poc00.domain.preparationfile.PreparationFile;
import com.inetpsa.poc00.domain.preparationfile.PreparationFileFactory;
import com.inetpsa.poc00.domain.preparationfile.PreparationFileRepository;
import com.inetpsa.poc00.domain.preparationfilestructure.PreparationFileStructure;
import com.inetpsa.poc00.domain.preparationresult.PreparationResult;
import com.inetpsa.poc00.domain.preparationresult.PreparationResultFactory;
import com.inetpsa.poc00.domain.vehiclefile.VehicleFileRepository;
import com.inetpsa.poc00.rest.pfstructure.PreparationFileStructureFinder;

/**
 * The Class PreparationFileResource.
 */
@Path("/preparationFile")
@Transactional
@JpaUnit(Config.JPA_UNIT)
public class PreparationFileResource {

	/** The prep file finder. */
	@Inject
	PreparationFileFinder prepFileFinder;

	/** The prep file assembler. */
	@Inject
	PreparationFileAssembler prepFileAssembler;

	/** The prep file repo. */
	@Inject
	PreparationFileRepository prepFileRepo;

	/** The prep file factory. */
	@Inject
	PreparationFileFactory prepFileFactory;

	/** The perp file struct finder. */
	@Inject
	PreparationFileStructureFinder perpFileStructFinder;

	/** The prep check list factory. */
	@Inject
	PreparationCheckListFactory prepCheckListFactory;

	/** The prep result factory. */
	@Inject
	PreparationResultFactory prepResultFactory;

	/** The vehicle file repo. */
	@Inject
	VehicleFileRepository vehicleFileRepo;

	/** The property lang. */
	PropertiesLang propertyLang = PropertiesLang.getInstance();

	/**
	 * Gets the preparation file.
	 * 
	 * @param vehicleFileId the vehicle file id
	 * @return the preparation file
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/preparationfilesdata/{vehicleFileId}")
	public Response getPreparationFile(@PathParam("vehicleFileId") Long vehicleFileId) {

		PreparationFile preparationFile = prepFileFinder.findPrepFileByVehicleFileId(vehicleFileId);

		PreparationFileRepresentation prepFileRepresentation = new PreparationFileRepresentation();

		if (preparationFile != null) {
			prepFileAssembler.doAssembleDtoFromAggregate(prepFileRepresentation, preparationFile);
		}

		createPreparationFile(1L);
		
		return Response.ok(prepFileRepresentation).build();

	}

	/**
	 * Save preparation file.
	 * 
	 * @param prepFileRepresentation the prep file representation
	 * @return the response
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/preparationfile")
	public Response savePreparationFile(PreparationFileRepresentation prepFileRepresentation) {

		PreparationFile preparationFile = prepFileFactory.createPreparationFile();

		prepFileAssembler.doMergeAggregateWithDto(preparationFile, prepFileRepresentation);

		prepFileRepo.savePreparationFile(preparationFile);

		return Response.ok().build();

	}


	/**
	 * Creates the preparation file.
	 * 
	 * @param vehicleFile the vehicle file
	 * @return the response
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/PreparationFile")
	public Response createPreparationFile(Long vehicleFileId) {

		PreparationFile preparationFile = prepFileFactory.createPreparationFile();

		preparationFile.setVehicleFile(vehicleFileRepo.load(vehicleFileId));

		PreparationFileStructure prepFileStructure = perpFileStructFinder.getLatestPrepFileStructure();

		preparationFile.setPrepFileStructure(prepFileStructure);

		// Create Preparation CheckList

		// PreparationCheckList 1)ByDefault
		List<PreparationCheckList> pclList = copyGenericPrepCheckList(preparationFile, prepFileStructure);
		preparationFile.setPreparationCheckList(pclList);

		// 2) Copy From Structure
		List<PreparationCheckList> defaultPrepCheckList = getDefaultPreparationCheckList(preparationFile);

		preparationFile.getPreparationCheckList().addAll(defaultPrepCheckList);

		return Response.ok().build();
	}

	/**
	 * Copy generic prep check list.
	 * 
	 * @param preparationFile the preparation file
	 * @param prepFileStructure the prep file structure
	 * @return the list
	 */
	private List<PreparationCheckList> copyGenericPrepCheckList(PreparationFile preparationFile, PreparationFileStructure prepFileStructure) {

		List<PreparationCheckList> pclList = new ArrayList<>();

		if (prepFileStructure != null && prepFileStructure.getPreparationCheckLists() != null && !prepFileStructure.getPreparationCheckLists().isEmpty()) {
			for (GenericPreparationChecklist pfs : prepFileStructure.getPreparationCheckLists()) {
				PreparationCheckList preparationCheckList = prepCheckListFactory.createPreparationCheckList();
				preparationCheckList.setDescription(pfs.getDescription());
				preparationCheckList.setEnabled(pfs.isEnabled());
				preparationCheckList.setOrder(pfs.getOrder());
				preparationCheckList.setLabel(pfs.getLabel());
				preparationCheckList.setTypeOfList(1);
				preparationCheckList.setPreparationFile(preparationFile);

				List<PreparationResult> prepResult = copyGenericPreparationItem(pfs);
				preparationCheckList.setPreparationResultList(prepResult);

				pclList.add(preparationCheckList);
			}
		}

		return pclList;
	}

	/**
	 * Copy generic preparation item.
	 * 
	 * @param pfs the pfs
	 * @param preparationCheckList the preparation check list
	 * @return the list
	 */
	private List<PreparationResult> copyGenericPreparationItem(GenericPreparationChecklist pfs) {

		List<PreparationResult> prepResultList = new ArrayList<>();

		if (pfs.getPreparationItems() != null && !pfs.getPreparationItems().isEmpty()) {

			for (GenericPreparationItem gpi : pfs.getPreparationItems()) {
				PreparationResult preparationResult = prepResultFactory.createPreparationResult();
				preparationResult.setDataType(gpi.getDataType());
				preparationResult.setUnit(gpi.getUnit());
				preparationResult.setOrder(gpi.getOrder());
				preparationResult.setHelpText(gpi.getHelpText());
				preparationResult.setMandatory(gpi.isMandatory());
				preparationResult.setLabel(gpi.getLabel());
				preparationResult.setAuthorizedComment(gpi.isAuthorizedComment());

				prepResultList.add(preparationResult);
			}
		}

		return prepResultList;
	}

	/**
	 * Gets the default preparation check list.
	 * 
	 * @param preparationFile the preparation file
	 * @return the default preparation check list
	 */

	private List<PreparationCheckList> getDefaultPreparationCheckList(PreparationFile preparationFile) {

		List<PreparationCheckList> preparationCheckList = new ArrayList<>();
		
		preparationCheckList.add(getCalBvaPCL(preparationFile));
		preparationCheckList.add(getCalMoteurPCL(preparationFile));
		preparationCheckList.add(getOilLevelCheckPCL(preparationFile));
		preparationCheckList.add(getMotGasoline(preparationFile));
		preparationCheckList.add(getMotDiesel(preparationFile));

		return preparationCheckList;
	}

	/**
	 * @param preparationFile
	 */

	private PreparationCheckList getCalBvaPCL(PreparationFile preparationFile) {

		PreparationCheckList calculatorBVA = prepCheckListFactory.createPreparationCheckList();

		calculatorBVA.setLabel(propertyLang.getProperty("pl.calculatorbva.label"));
		calculatorBVA.setDescription(propertyLang.getProperty("pl.calculatorbva.description"));
		calculatorBVA.setOrder(100);
		calculatorBVA.setTypeOfList(2);
		calculatorBVA.setEnabled(checkBoolean(propertyLang.getProperty("pl.calculatorbva.enable")));
		calculatorBVA.setPreparationFile(preparationFile);

		List<PreparationResult> prepResultList = new ArrayList<>();

		PreparationResult marque = prepResultFactory.createPreparationResult();
		marque.setLabel(propertyLang.getProperty("pl.calculatorbva.marque.label")); 
		marque.setOrder(1); 
		marque.setDataType(propertyLang.getProperty("pl.calculatorbva.marque.dataType"));
		marque.setConformity(checkBoolean(propertyLang.getProperty("pl.calculatorbva.marque.conformity")));
		marque.setMandatory(checkBoolean(propertyLang.getProperty("pl.calculatorbva.marque.mandatory")));
		marque.setUnit(checkNull(propertyLang.getProperty("pl.calculatorbva.marque.unit")));
		marque.setHelpText(checkNull(propertyLang.getProperty("pl.calculatorbva.marque.helpText")));
		marque.setAuthorizedComment(checkNull(propertyLang.getProperty("pl.calculatorbva.marque.authorizedComment")));
		marque.setPreparationCheckList(calculatorBVA);
		prepResultList.add(marque);

		PreparationResult nHard = prepResultFactory.createPreparationResult();
		nHard.setLabel(propertyLang.getProperty("pl.calculatorbva.nhard.label"));  
		nHard.setOrder(2);
		nHard.setDataType(propertyLang.getProperty("pl.calculatorbva.nhard.dataType")); 
		nHard.setConfonmity(checkBoolean(propertyLang.getProperty("pl.calculatorbva.nhard.conformity")));
		nHard.setMandatory(checkBoolean(propertyLang.getProperty("pl.calculatorbva.nhard.mandatory")));
		nHard.setUnit(checkNull(propertyLang.getProperty("pl.calculatorbva.nhard.unit")));
		nHard.setHelpText(checkNull(propertyLang.getProperty("pl.calculatorbva.nhard.helpText")));
		nHard.setAuthorizedComment(checkNull(propertyLang.getProperty("pl.calculatorbva.nhard.authorizedComment")));
		nHard.setPreparationCheckList(calculatorBVA);
		prepResultList.add(nHard);

		PreparationResult nSoft = prepResultFactory.createPreparationResult();
		nSoft.setLabel(propertyLang.getProperty("pl.calculatorbva.nsoft.label"));
		nSoft.setOrder(3);
		nSoft.setDataType(propertyLang.getProperty("pl.calculatorbva.nhard.authorizedComment"));
		nSoft.setConformity(checkBoolean(propertyLang.getProperty("pl.calculatorbva.nsoft.conformity")));
		nSoft.setMandatory(checkBoolean(propertyLang.getProperty("pl.calculatorbva.nsoft.mandatory")));
		nSoft.setUnit(checkNull(propertyLang.getProperty("pl.calculatorbva.nsoft.unit")));
		nSoft.setHelpText(checkNull(propertyLang.getProperty("pl.calculatorbva.nsoft.helpText")));
		nSoft.setAuthorizedComment(checkNull(propertyLang.getProperty("pl.calculatorbva.nhard.authorizedComment")));
		nSoft.setPreparationCheckList(calculatorBVA);
		prepResultList.add(nSoft);

		calculatorBVA.setPreparationResultList(prepResultList);

		return calculatorBVA;
	}

	private PreparationCheckList getCalMoteurPCL(PreparationFile preparationFile) {

		PreparationCheckList calculatorMoteur = prepCheckListFactory.createPreparationCheckList();

		calculatorMoteur.setPreparationFile(preparationFile);
		calculatorMoteur.setLabel(propertyLang.getProperty("pl.calculatormoteur.label"));
		calculatorMoteur.setDescription(propertyLang.getProperty("pl.calculatormoteur.description"));
		calculatorMoteur.setOrder(101);
		calculatorMoteur.setTypeOfList(2);
		calculatorMoteur.setEnabled(checkBoolean(propertyLang.getProperty("pl.calculatormoteur.enable")));
		
		List<PreparationResult> prepResultList = new ArrayList<>();

		PreparationResult marque = prepResultFactory.createPreparationResult();
		marque.setLabel(propertyLang.getProperty("pl.calculatorbva.marque.label"));
		marque.setDataType(propertyLang.getProperty("pl.calculatormoteur.marque.dataType"));
		marque.setOrder(1);
		marque.setMandatory(checkBoolean(propertyLang.getProperty("pl.calculatorbva.marque.mandatory")));
		marque.setConformity(checkBoolean(propertyLang.getProperty("pl.calculatorbva.marque.confirmity")));
		marque.setUnit(checkNull(property.getProperty("calculatormoteur.marque.unit")));
		marque.setHelpText(checkNull(propertyLang.getProperty("pl.calculatorbva.marque.helpText")));
		marque.setAuthorizedComment(checkNull(propertyLang.getProperty("pl.calculatorbva.marque.authorizedComment")));
		marque.setPreparationCheckList(calculatorMoteur);
		prepResultList.add(marque);

		PreparationResult nHard = prepResultFactory.createPreparationResult();
		nHard.setLabel(propertyLang.getProperty("pl.calculatormoteur.nhard.label"));
		nHard.setDataType(propertyLang.getProperty("pl.calculatormoteur.nhard.dataType")); 
		nHard.setOrder(2);
		nHard.setMandatory(checkBoolean(propertyLang.getProperty("pl.calculatormoteur.nhard.mandatory")));
		nHard.setConformity(checkBoolean(propertyLang.getProperty("pl.calculatormoteur.nhard.conformity")));
		nHard.setUnit(checkNull(propertyLang.getProperty("pl.calculatormoteur.nhard.unit")));
		nHard.setHelpText(checkNull(propertyLang.getProperty("pl.calculatormoteur.nhard.helpText")));
		nHard.setAuthorizedComment(checkNull(propertyLang.getProperty("pl.calculatormoteur.nhard.authorizedComment")));
		nHard.setPreparationCheckList(calculatorMoteur);
		prepResultList.add(nHard);

		PreparationResult nSoft = prepResultFactory.createPreparationResult();
		nSoft.setLabel(propertyLang.getProperty("pl.calculatormoteur.nsoft.label"));
		nSoft.setDataType(propertyLang.getProperty("pl.calculatormoteur.nsoft.dataType"));
		nSoft.setOrder(3);
		nSoft.setMandatory(checkBoolean(propertyLang.getProperty("pl.calculatormoteur.nsoft.mandatory")));
		nSoft.setConformity(checkBoolean(propertyLang.getProperty("pl.calculatormoteur.nsoft.conformity")));
		nSoft.setUnit(checkNull(propertyLang.getProperty("pl.calculatormoteur.nsoft.unit")));
		nSoft.setHelpText(checkNull(propertyLang.getProperty("pl.calculatormoteur.nsoft.helpText")));
		nSoft.setAuthorizedComment(checkNull(propertyLang.getProperty("pl.calculatormoteur.nsoft.authorizedComment")));
		nSoft.setPreparationCheckList(calculatorMoteur);
		prepResultList.add(nSoft);

		calculatorMoteur.setPreparationResultList(prepResultList);

		return calculatorMoteur;

	}
	
	private PreparationCheckList getMotDiesel(PreparationFile preparationFile) {
		
		PreparationCheckList motDiesel = prepCheckListFactory.createPreparationCheckList();

		motDiesel.setPreparationFile(preparationFile);
		motDiesel.setLabel(propertyLang.getProperty("pl.motdiesel.label"));
		motDiesel.setDescription(propertyLang.getProperty("pl.motdiesel.description"));
		motDiesel.setOrder(130);
		motDiesel.setTypeOfList(35);
		motDiesel.setEnabled(checkBoolean(propertyLang.getProperty("pl.motdiesel.enable")));
		
		List<PreparationResult> prepResultList = new ArrayList<>();

		PreparationResult m1Opacity = prepResultFactory.createPreparationResult();
		m1Opacity.setLabel(propertyLang.getProperty("pl.motdiesel.m1opacity.label"));
		m1Opacity.setDataType(propertyLang.getProperty("pl.motdiesel.m1opacity.dataType"));
		m1Opacity.setOrder(1);
		m1Opacity.setMandatory(checkBoolean(propertyLang.getProperty("pl.motdiesel.m1opacity.mandatory")));
		m1Opacity.setConformity(checkBoolean(propertyLang.getProperty("pl.motdiesel.m1opacity.conformity")));
		m1Opacity.setUnit(checkNull(property.getProperty("motdiesel.m1opacity.unit")));
		m1Opacity.setHelpText(checkNull(propertyLang.getProperty("pl.motdiesel.m1opacity.helpText")));
		m1Opacity.setAuthorizedComment(checkNull(propertyLang.getProperty("pl.motdiesel.m1opacity.authorizedComment")));
		m1Opacity.setPreparationCheckList(motDiesel);
		prepResultList.add(m1Opacity);

		PreparationResult m2Opacity = prepResultFactory.createPreparationResult();
		m2Opacity.setLabel(propertyLang.getProperty("pl.motdiesel.m2opacity.label"));
		m2Opacity.setDataType(propertyLang.getProperty("pl.motdiesel.m2opacity.dataType")); 
		m2Opacity.setOrder(2);
		m2Opacity.setMandatory(checkBoolean(propertyLang.getProperty("pl.motdiesel.m2opacity.mandatory")));
		m2Opacity.setConformity(checkBoolean(propertyLang.getProperty("pl.motdiesel.m2opacity.conformity")));
		m2Opacity.setUnit(checkNull(propertyLang.getProperty("pl.motdiesel.m2opacity.unit")));
		m2Opacity.setHelpText(checkNull(propertyLang.getProperty("pl.motdiesel.m2opacity.helpText")));
		m2Opacity.setAuthorizedComment(checkNull(propertyLang.getProperty("pl.motdiesel.m2opacity.authorizedComment")));
		m2Opacity.setPreparationCheckList(motDiesel);
		prepResultList.add(m2Opacity);

		PreparationResult m3Opacity = prepResultFactory.createPreparationResult();
		m3Opacity.setLabel(propertyLang.getProperty("pl.motdiesel.m3opacity.label"));
		m3Opacity.setDataType(propertyLang.getProperty("pl.motdiesel.m3opacity.dataType"));
		m3Opacity.setOrder(3);
		m3Opacity.setMandatory(checkBoolean(propertyLang.getProperty("pl.motdiesel.m3opacity.mandatory")));
		m3Opacity.setConformity(checkBoolean(propertyLang.getProperty("pl.motdiesel.m3opacity.conformity")));
		m3Opacity.setUnit(checkNull(propertyLang.getProperty("pl.motdiesel.m3opacity.unit")));
		m3Opacity.setHelpText(checkNull(propertyLang.getProperty("pl.motdiesel.m3opacity.helpText")));
		m3Opacity.setAuthorizedComment(checkNull(propertyLang.getProperty("pl.motdiesel.m3opacity.authorizedComment")));
		m3Opacity.setPreparationCheckList(motDiesel);
		prepResultList.add(m3Opacity);

		PreparationResult m4Opacity = prepResultFactory.createPreparationResult();
		m4Opacity.setLabel(propertyLang.getProperty("pl.motdiesel.m4opacity.label"));
		m4Opacity.setDataType(propertyLang.getProperty("pl.motdiesel.m4opacity.dataType"));
		m4Opacity.setOrder(4);
		m4Opacity.setMandatory(checkBoolean(propertyLang.getProperty("pl.motdiesel.m4opacity.mandatory")));
		m4Opacity.setConformity(checkBoolean(propertyLang.getProperty("pl.motdiesel.m4opacity.conformity")));
		m4Opacity.setUnit(checkNull(propertyLang.getProperty("pl.motdiesel.m4opacity.unit")));
		m4Opacity.setHelpText(checkNull(propertyLang.getProperty("pl.motdiesel.m4opacity.helpText")));
		m4Opacity.setAuthorizedComment(checkNull(propertyLang.getProperty("pl.motdiesel.m4opacity.authorizedComment")));
		m4Opacity.setPreparationCheckList(motDiesel);
		prepResultList.add(m4Opacity);
		
		PreparationResult moyenneOpacity = prepResultFactory.createPreparationResult();
		moyenneOpacity.setLabel(propertyLang.getProperty("pl.motdiesel.moyenneopacity.label"));
		moyenneOpacity.setDataType(propertyLang.getProperty("pl.motdiesel.moyenneopacity.dataType"));
		moyenneOpacity.setOrder(5);
		moyenneOpacity.setMandatory(checkBoolean(propertyLang.getProperty("pl.motdiesel.moyenneopacity.mandatory")));
		moyenneOpacity.setConformity(checkBoolean(propertyLang.getProperty("pl.motdiesel.moyenneopacity.conformity")));
		moyenneOpacity.setUnit(checkNull(propertyLang.getProperty("pl.motdiesel.moyenneopacity.unit")));
		moyenneOpacity.setHelpText(checkNull(propertyLang.getProperty("pl.motdiesel.moyenneopacity.helpText")));
		moyenneOpacity.setAuthorizedComment(checkNull(propertyLang.getProperty("pl.motdiesel.moyenneopacity.authorizedComment")));
		moyenneOpacity.setPreparationCheckList(motDiesel);
		prepResultList.add(moyenneOpacity);
		
		PreparationResult tempHuile = prepResultFactory.createPreparationResult();
		tempHuile.setLabel(propertyLang.getProperty("pl.motdiesel.temphuile.label"));
		tempHuile.setDataType(propertyLang.getProperty("pl.motdiesel.temphuile.dataType"));
		tempHuile.setOrder(6);
		tempHuile.setMandatory(checkBoolean(propertyLang.getProperty("pl.motdiesel.temphuile.mandatory")));
		tempHuile.setConformity(checkBoolean(propertyLang.getProperty("pl.motdiesel.temphuile.conformity")));
		tempHuile.setUnit(checkNull(propertyLang.getProperty("pl.motdiesel.temphuile.unit")));
		tempHuile.setHelpText(checkNull(propertyLang.getProperty("pl.motdiesel.temphuile.helpText")));
		tempHuile.setAuthorizedComment(checkNull(propertyLang.getProperty("pl.motdiesel.temphuile.authorizedComment")));
		tempHuile.setPreparationCheckList(motDiesel);
		prepResultList.add(tempHuile);
		
		PreparationResult precisionAtom = prepResultFactory.createPreparationResult();
		precisionAtom.setLabel(propertyLang.getProperty("pl.motdiesel.precisionatom.label"));
		precisionAtom.setDataType(propertyLang.getProperty("pl.motdiesel.precisionatom.dataType"));
		precisionAtom.setOrder(7);
		precisionAtom.setMandatory(checkBoolean(propertyLang.getProperty("pl.motdiesel.precisionatom.mandatory")));
		precisionAtom.setConformity(checkBoolean(propertyLang.getProperty("pl.motdiesel.precisionatom.conformity")));
		precisionAtom.setUnit(checkNull(propertyLang.getProperty("pl.motdiesel.precisionatom.unit")));
		precisionAtom.setHelpText(checkNull(propertyLang.getProperty("pl.motdiesel.precisionatom.helpText")));
		precisionAtom.setAuthorizedComment(checkNull(propertyLang.getProperty("pl.motdiesel.precisionatom.authorizedComment")));
		precisionAtom.setPreparationCheckList(motDiesel);
		prepResultList.add(precisionAtom);
		
		PreparationResult tempAmbiante = prepResultFactory.createPreparationResult();
		tempAmbiante.setLabel(propertyLang.getProperty("pl.motdiesel.tempambiante.label"));
		tempAmbiante.setDataType(propertyLang.getProperty("pl.motdiesel.tempambiante.dataType"));
		tempAmbiante.setOrder(8);
		tempAmbiante.setMandatory(checkBoolean(propertyLang.getProperty("pl.motdiesel.tempambiante.mandatory")));
		tempAmbiante.setConformity(checkBoolean(propertyLang.getProperty("pl.motdiesel.tempambiante.conformity")));
		tempAmbiante.setUnit(checkNull(propertyLang.getProperty("pl.motdiesel.tempambiante.unit")));
		tempAmbiante.setHelpText(checkNull(propertyLang.getProperty("pl.motdiesel.tempambiante.helpText")));
		tempAmbiante.setAuthorizedComment(checkNull(propertyLang.getProperty("pl.motdiesel.tempambiante.authorizedComment")));
		tempAmbiante.setPreparationCheckList(motDiesel);
		prepResultList.add(tempAmbiante);
		
		PreparationResult regimeMaxi = prepResultFactory.createPreparationResult();
		regimeMaxi.setLabel(propertyLang.getProperty("pl.motdiesel.regimemaxi.label"));
		regimeMaxi.setDataType(propertyLang.getProperty("pl.motdiesel.regimemaxi.dataType"));
		regimeMaxi.setOrder(9);
		regimeMaxi.setMandatory(checkBoolean(propertyLang.getProperty("pl.motdiesel.regimemaxi.mandatory")));
		regimeMaxi.setConformity(checkBoolean(propertyLang.getProperty("pl.motdiesel.regimemaxi.conformity")));
		regimeMaxi.setUnit(checkNull(propertyLang.getProperty("pl.motdiesel.regimemaxi.unit")));
		regimeMaxi.setHelpText(checkNull(propertyLang.getProperty("pl.motdiesel.regimemaxi.helpText")));
		regimeMaxi.setAuthorizedComment(checkNull(propertyLang.getProperty("pl.motdiesel.regimemaxi.authorizedComment")));
		regimeMaxi.setPreparationCheckList(motDiesel);
		prepResultList.add(regimeMaxi);
		
		PreparationResult ralenti = prepResultFactory.createPreparationResult();
		ralenti.setLabel(propertyLang.getProperty("pl.motdiesel.ralenti.label"));
		ralenti.setDataType(propertyLang.getProperty("pl.motdiesel.ralenti.dataType"));
		ralenti.setOrder(10);
		ralenti.setMandatory(checkBoolean(propertyLang.getProperty("pl.motdiesel.ralenti.mandatory")));
		ralenti.setConformity(checkBoolean(propertyLang.getProperty("pl.motdiesel.ralenti.conformity")));
		ralenti.setUnit(checkNull(propertyLang.getProperty("pl.motdiesel.ralenti.unit")));
		ralenti.setHelpText(checkNull(propertyLang.getProperty("pl.motdiesel.ralenti.helpText")));
		ralenti.setAuthorizedComment(checkNull(propertyLang.getProperty("pl.motdiesel.ralenti.authorizedComment")));
		ralenti.setPreparationCheckList(motDiesel);
		prepResultList.add(ralenti);

		PreparationResult valuerOpacite = prepResultFactory.createPreparationResult();
		valuerOpacite.setLabel(propertyLang.getProperty("pl.motdiesel.valueropacite.label"));
		valuerOpacite.setDataType(propertyLang.getProperty("pl.motdiesel.valueropacite.dataType"));
		valuerOpacite.setOrder(11);
		valuerOpacite.setMandatory(checkBoolean(propertyLang.getProperty("pl.motdiesel.valueropacite.mandatory")));
		valuerOpacite.setConformity(checkBoolean(propertyLang.getProperty("pl.motdiesel.valueropacite.conformity")));
		valuerOpacite.setUnit(checkNull(propertyLang.getProperty("pl.motdiesel.valueropacite.unit")));
		valuerOpacite.setHelpText(checkNull(propertyLang.getProperty("pl.motdiesel.valueropacite.helpText")));
		valuerOpacite.setAuthorizedComment(checkNull(propertyLang.getProperty("pl.motdiesel.valueropacite.authorizedComment")));
		valuerOpacite.setPreparationCheckList(motDiesel);
		prepResultList.add(valuerOpacite);
		
		PreparationResult opaciteRegle = prepResultFactory.createPreparationResult();
		opaciteRegle.setLabel(propertyLang.getProperty("pl.motdiesel.opaciterégle.label"));
		opaciteRegle.setDataType(propertyLang.getProperty("pl.motdiesel.opaciterégle.dataType"));
		opaciteRegle.setOrder(12);
		opaciteRegle.setMandatory(checkBoolean(propertyLang.getProperty("pl.motdiesel.opaciterégle.mandatory")));
		opaciteRegle.setConformity(checkBoolean(propertyLang.getProperty("pl.motdiesel.opaciterégle.conformity")));
		opaciteRegle.setUnit(checkNull(propertyLang.getProperty("pl.motdiesel.opaciterégle.unit")));
		opaciteRegle.setHelpText(checkNull(propertyLang.getProperty("pl.motdiesel.opaciterégle.helpText")));
		opaciteRegle.setAuthorizedComment(checkNull(propertyLang.getProperty("pl.motdiesel.opaciterégle.authorizedComment")));
		opaciteRegle.setPreparationCheckList(motDiesel);
		prepResultList.add(opaciteRegle);
		
		motDiesel.setPreparationResultList(prepResultList);

		return motDiesel;
	}

	private PreparationCheckList getMotGasoline(PreparationFile preparationFile) {
		
		PreparationCheckList motGasoline = prepCheckListFactory.createPreparationCheckList();

		motGasoline.setPreparationFile(preparationFile);
		motGasoline.setLabel(propertyLang.getProperty("pl.motgasoline.label"));
		motGasoline.setDescription(propertyLang.getProperty("pl.motgasoline.description"));
		motGasoline.setEnabled(checkBoolean(propertyLang.getProperty("pl.motgasoline.enable")));
		motGasoline.setOrder(120);
		motGasoline.setTypeOfList(4);
		
		List<PreparationResult> prepResultList = new ArrayList<>();

		PreparationResult ralenti = prepResultFactory.createPreparationResult();
		ralenti.setLabel(propertyLang.getProperty("pl.motgasoline.ralenti.label"));
		ralenti.setDataType(propertyLang.getProperty("pl.motgasoline.ralenti.dataType"));
		ralenti.setOrder(1);
		ralenti.setMandatory(checkBoolean(propertyLang.getProperty("pl.motgasoline.ralenti.mandatory")));
		ralenti.setConformity(checkBoolean(propertyLang.getProperty("pl.motgasoline.ralenti.conformity")));
		ralenti.setUnit(checkNull(property.getProperty("motgasoline.ralenti.unit")));
		ralenti.setHelpText(checkNull(propertyLang.getProperty("pl.motgasoline.ralenti.helpText")));
		ralenti.setAuthorizedComment(checkNull(propertyLang.getProperty("pl.motgasoline.ralenti.authorizedComment")));
		ralenti.setPreparationCheckList(motGasoline);
		prepResultList.add(ralenti);
		
		PreparationResult co2Ralenti = prepResultFactory.createPreparationResult();
		co2Ralenti.setLabel(propertyLang.getProperty("pl.motgasoline.co2ralenti.label"));
		co2Ralenti.setDataType(propertyLang.getProperty("pl.motgasoline.co2ralenti.dataType"));
		co2Ralenti.setOrder(2);
		co2Ralenti.setMandatory(checkBoolean(propertyLang.getProperty("pl.motgasoline.co2ralenti.mandatory")));
		co2Ralenti.setConformity(checkBoolean(propertyLang.getProperty("pl.motgasoline.co2ralenti.conformity")));
		co2Ralenti.setUnit(checkNull(property.getProperty("motgasoline.co2ralenti.unit")));
		co2Ralenti.setHelpText(checkNull(propertyLang.getProperty("pl.motgasoline.co2ralenti.helpText")));
		co2Ralenti.setAuthorizedComment(checkNull(propertyLang.getProperty("pl.motgasoline.co2ralenti.authorizedComment")));
		co2Ralenti.setPreparationCheckList(motGasoline);
		prepResultList.add(co2Ralenti);
		
		PreparationResult hcRalenti = prepResultFactory.createPreparationResult();
		hcRalenti.setLabel(propertyLang.getProperty("pl.motgasoline.hcralenti.label"));
		hcRalenti.setDataType(propertyLang.getProperty("pl.motgasoline.hcralenti.dataType"));
		hcRalenti.setOrder(3);
		hcRalenti.setMandatory(checkBoolean(propertyLang.getProperty("pl.motgasoline.hcralenti.mandatory")));
		hcRalenti.setConformity(checkBoolean(propertyLang.getProperty("pl.motgasoline.hcralenti.conformity")));
		hcRalenti.setUnit(checkNull(property.getProperty("motgasoline.hcralenti.unit")));
		hcRalenti.setHelpText(checkNull(propertyLang.getProperty("pl.motgasoline.hcralenti.helpText")));
		hcRalenti.setAuthorizedComment(checkNull(propertyLang.getProperty("pl.motgasoline.hcralenti.authorizedComment")));
		hcRalenti.setPreparationCheckList(motGasoline);
		prepResultList.add(hcRalenti);
		
		PreparationResult coRalenti = prepResultFactory.createPreparationResult();
		coRalenti.setLabel(propertyLang.getProperty("pl.motgasoline.coralenti.label"));
		coRalenti.setDataType(propertyLang.getProperty("pl.motgasoline.coralenti.dataType"));
		coRalenti.setOrder(4);
		coRalenti.setMandatory(checkBoolean(propertyLang.getProperty("pl.motgasoline.coralenti.mandatory")));
		coRalenti.setConformity(checkBoolean(propertyLang.getProperty("pl.motgasoline.coralenti.conformity")));
		coRalenti.setUnit(checkNull(property.getProperty("motgasoline.coralenti.unit")));
		coRalenti.setHelpText(checkNull(propertyLang.getProperty("pl.motgasoline.coralenti.helpText")));
		coRalenti.setAuthorizedComment(checkNull(propertyLang.getProperty("pl.motgasoline.coralenti.authorizedComment")));
		coRalenti.setPreparationCheckList(motGasoline);
		prepResultList.add(coRalenti);
		
		PreparationResult ralentiAcc = prepResultFactory.createPreparationResult();
		ralentiAcc.setLabel(propertyLang.getProperty("pl.motgasoline.ralentiacc.label"));
		ralentiAcc.setDataType(propertyLang.getProperty("pl.motgasoline.ralentiacc.dataType"));
		ralentiAcc.setOrder(5);
		ralentiAcc.setMandatory(checkBoolean(propertyLang.getProperty("pl.motgasoline.ralentiacc.mandatory")));
		ralentiAcc.setConformity(checkBoolean(propertyLang.getProperty("pl.motgasoline.ralentiacc.conformity")));
		ralentiAcc.setUnit(checkNull(property.getProperty("motgasoline.ralentiacc.unit")));
		ralentiAcc.setHelpText(checkNull(propertyLang.getProperty("pl.motgasoline.ralentiacc.helpText")));
		ralentiAcc.setAuthorizedComment(checkNull(propertyLang.getProperty("pl.motgasoline.ralentiacc.authorizedComment")));
		ralentiAcc.setPreparationCheckList(motGasoline);
		prepResultList.add(ralentiAcc);
		
		PreparationResult coralentiAcc = prepResultFactory.createPreparationResult();
		coralentiAcc.setLabel(propertyLang.getProperty("pl.motgasoline.coralentiacc.label"));
		coralentiAcc.setDataType(propertyLang.getProperty("pl.motgasoline.coralentiacc.dataType"));
		coralentiAcc.setOrder(6);
		coralentiAcc.setMandatory(checkBoolean(propertyLang.getProperty("pl.motgasoline.coralentiacc.mandatory")));
		coralentiAcc.setConformity(checkBoolean(propertyLang.getProperty("pl.motgasoline.coralentiacc.conformity")));
		coralentiAcc.setUnit(checkNull(property.getProperty("motgasoline.coralentiacc.unit")));
		coralentiAcc.setHelpText(checkNull(propertyLang.getProperty("pl.motgasoline.coralentiacc.helpText")));
		coralentiAcc.setAuthorizedComment(checkNull(propertyLang.getProperty("pl.motgasoline.coralentiacc.authorizedComment")));
		coralentiAcc.setPreparationCheckList(motGasoline);
		prepResultList.add(coralentiAcc);
		
		PreparationResult lambdaRalentiAcc = prepResultFactory.createPreparationResult();
		lambdaRalentiAcc.setLabel(propertyLang.getProperty("pl.motgasoline.lambdaralentiacc.label"));
		lambdaRalentiAcc.setDataType(propertyLang.getProperty("pl.motgasoline.lambdaralentiacc.dataType"));
		lambdaRalentiAcc.setOrder(7);
		lambdaRalentiAcc.setMandatory(checkBoolean(propertyLang.getProperty("pl.motgasoline.lambdaralentiacc.mandatory")));
		lambdaRalentiAcc.setConformity(checkBoolean(propertyLang.getProperty("pl.motgasoline.lambdaralentiacc.conformity")));
		lambdaRalentiAcc.setUnit(checkNull(property.getProperty("motgasoline.lambdaralentiacc.unit")));
		lambdaRalentiAcc.setHelpText(checkNull(propertyLang.getProperty("pl.motgasoline.lambdaralentiacc.helpText")));
		lambdaRalentiAcc.setAuthorizedComment(checkNull(propertyLang.getProperty("pl.motgasoline.lambdaralentiacc.authorizedComment")));
		lambdaRalentiAcc.setPreparationCheckList(motGasoline);
		prepResultList.add(lambdaRalentiAcc);
		
		PreparationResult decisionCo = prepResultFactory.createPreparationResult();
		decisionCo.setLabel(propertyLang.getProperty("pl.motgasoline.decisionco.label"));
		decisionCo.setDataType(propertyLang.getProperty("pl.motgasoline.decisionco.dataType"));
		decisionCo.setOrder(8);
		decisionCo.setMandatory(checkBoolean(propertyLang.getProperty("pl.motgasoline.decisionco.mandatory")));
		decisionCo.setConformity(checkBoolean(propertyLang.getProperty("pl.motgasoline.decisionco.conformity")));
		decisionCo.setUnit(checkNull(property.getProperty("motgasoline.decisionco.unit")));
		decisionCo.setHelpText(checkNull(propertyLang.getProperty("pl.motgasoline.decisionco.helpText")));
		decisionCo.setAuthorizedComment(checkNull(propertyLang.getProperty("pl.motgasoline.decisionco.authorizedComment")));
		decisionCo.setPreparationCheckList(motGasoline);
		prepResultList.add(decisionCo);
		
		PreparationResult decisionLambda = prepResultFactory.createPreparationResult();
		decisionLambda.setLabel(propertyLang.getProperty("pl.motgasoline.decisionlambda.label"));
		decisionLambda.setDataType(propertyLang.getProperty("pl.motgasoline.decisionlambda.dataType"));
		decisionLambda.setOrder(9);
		decisionLambda.setMandatory(checkBoolean(propertyLang.getProperty("pl.motgasoline.decisionclambda.mandatory")));
		decisionLambda.setConformity(checkBoolean(propertyLang.getProperty("pl.motgasoline.decisionlambda.conformity")));
		decisionLambda.setUnit(checkNull(property.getProperty("motgasoline.decisionlambda.unit")));
		decisionLambda.setHelpText(checkNull(propertyLang.getProperty("pl.motgasoline.decisionlambda.helpText")));
		decisionLambda.setAuthorizedComment(checkNull(propertyLang.getProperty("pl.motgasoline.decisionlambda.authorizedComment")));
		decisionLambda.setPreparationCheckList(motGasoline);
		prepResultList.add(decisionLambda);
		
		motGasoline.setPreparationResultList(prepResultList);

		return motGasoline;
	}

	private PreparationCheckList getOilLevelCheckPCL(PreparationFile preparationFile) {
		
		PreparationCheckList oilLevelCheck = prepCheckListFactory.createPreparationCheckList();

		oilLevelCheck.setPreparationFile(preparationFile);
		oilLevelCheck.setLabel(propertyLang.getProperty("pl.oillevelcheck.label"));
		oilLevelCheck.setDescription(propertyLang.getProperty("pl.oillevelcheck.description"));
		oilLevelCheck.setEnabled(checkBoolean(propertyLang.getProperty("pl.oillevelcheck.enable")));
		oilLevelCheck.setOrder(110);
		oilLevelCheck.setTypeOfList(3);
		
		List<PreparationResult> prepResultList = new ArrayList<>();

		PreparationResult jauge = prepResultFactory.createPreparationResult();
		jauge.setLabel(propertyLang.getProperty("pl.oillevelcheck.jauge.label"));
		jauge.setDataType(propertyLang.getProperty("pl.oillevelcheck.jauge.dataType"));
		jauge.setOrder(1);
		jauge.setMandatory(checkBoolean(propertyLang.getProperty("pl.oillevelcheck.jauge.mandatory")));
		jauge.setConformity(checkBoolean(propertyLang.getProperty("pl.oillevelcheck.jauge.conformity")));
		jauge.setUnit(checkNull(property.getProperty("oillevelcheck.jauge.unit")));
		jauge.setHelpText(checkNull(propertyLang.getProperty("pl.oillevelcheck.jauge.helpText")));
		jauge.setAuthorizedComment(checkNull(propertyLang.getProperty("pl.oillevelcheck.jauge.authorizedComment")));
		jauge.setPreparationCheckList(oilLevelCheck);
		prepResultList.add(jauge);
		
		PreparationResult remise = prepResultFactory.createPreparationResult();
		remise.setLabel(propertyLang.getProperty("pl.oillevelcheck.remise.label"));
		remise.setDataType(propertyLang.getProperty("pl.oillevelcheck.remise.dataType"));
		remise.setOrder(2);
		remise.setMandatory(checkBoolean(propertyLang.getProperty("pl.oillevelcheck.remise.mandatory")));
		remise.setConformity(checkBoolean(propertyLang.getProperty("pl.oillevelcheck.remise.conformity")));
		remise.setUnit(checkNull(property.getProperty("oillevelcheck.remise.unit")));
		remise.setHelpText(checkNull(propertyLang.getProperty("pl.oillevelcheck.remise.helpText")));
		remise.setAuthorizedComment(checkNull(propertyLang.getProperty("pl.oillevelcheck.remise.authorizedComment")));
		remise.setPreparationCheckList(oilLevelCheck);
		prepResultList.add(remise);
		
		PreparationResult ajout = prepResultFactory.createPreparationResult();
		ajout.setLabel(propertyLang.getProperty("pl.oillevelcheck.ajout.label"));
		ajout.setDataType(propertyLang.getProperty("pl.oillevelcheck.ajout.dataType"));
		ajout.setOrder(3);
		ajout.setMandatory(checkBoolean(propertyLang.getProperty("pl.oillevelcheck.ajout.mandatory")));
		ajout.setConformity(checkBoolean(propertyLang.getProperty("pl.oillevelcheck.ajout.conformity")));
		ajout.setUnit(checkNull(property.getProperty("oillevelcheck.ajout.unit")));
		ajout.setHelpText(checkNull(propertyLang.getProperty("pl.oillevelcheck.ajout.helpText")));
		ajout.setAuthorizedComment(checkNull(propertyLang.getProperty("pl.oillevelcheck.ajout.authorizedComment")));
		ajout.setPreparationCheckList(oilLevelCheck);
		prepResultList.add(ajout);
		
		oilLevelCheck.setPreparationResultList(prepResultList);

		return oilLevelCheck;
	}

	private String checkNull(String value) {
		
		if("null".equalsIgnoreCase(value)) {
			return null;
		}
		
		return value;
	}
	
	private Boolean checkBoolean(String value) {
		
		if(Boolean.TRUE.toString().equalsIgnoreCase(value)) {
			return Boolean.TRUE;
		} 
		
		return Boolean.FALSE;
	}
	
}
