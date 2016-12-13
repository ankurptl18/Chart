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

		preparationCheckList.add(getCalMoteurPCL(preparationFile));

		return preparationCheckList;
	}

	/**
	 * @param preparationFile
	 */

	private PreparationCheckList getCalBvaPCL(PreparationFile preparationFile) {

		PreparationCheckList calculatorBVA = prepCheckListFactory.createPreparationCheckList();

		calculatorBVA.setDescription(propertyLang.getProperty("calculatorbva.description"));
		
		String temp = propertyLang.getProperty("calculatorbva.enable");
		if(Boolean.TRUE.toString().equalsIgnoreCase(temp)){
			calculatorBVA.setEnabled(Boolean.TRUE); 
		} else {
			calculatorBVA.setEnabled(Boolean.FALSE);
		}
		
		calculatorBVA.setLabel(propertyLang.getProperty("calculatorbva.label"));
		calculatorBVA.setOrder(100);
		calculatorBVA.setPreparationFile(preparationFile);
		calculatorBVA.setTypeOfList(2);

		List<PreparationResult> prepResultList = new ArrayList<>();

		PreparationResult marque = prepResultFactory.createPreparationResult();
		
		marque.setDataType(propertyLang.getProperty("calculatorbva.marque.dataType"));
		
		temp = propertyLang.getProperty("calculatorbva.marque.unit");
		
		if(propertyLang.getProperty("calculatorbva.marque.unit").equalsIgnoreCase("null")) {
			marque.setUnit(null);
		}
			
		
		marque.setUnit(null);
		marque.setOrder(1);
		marque.setHelpText(null);
		marque.setMandatory(Boolean.TRUE);
		marque.setAuthorizedComment(null);
		marque.setLabel("MARQUE"); 
		marque.setConformity(Boolean.FALSE);
		marque.setPreparationCheckList(calculatorBVA);
		prepResultList.add(marque);

		PreparationResult nHard = prepResultFactory.createPreparationResult();
		nHard.setDataType("String");
		nHard.setUnit(null);
		nHard.setOrder(2);
		nHard.setHelpText(null);
		nHard.setMandatory(Boolean.TRUE);
		nHard.setAuthorizedComment(null);
		nHard.setLabel("N° HARD");
		nHard.setConformity(Boolean.FALSE);
		nHard.setPreparationCheckList(calculatorBVA);
		prepResultList.add(nHard);

		PreparationResult nSoft = prepResultFactory.createPreparationResult();
		nSoft.setDataType("String");
		nSoft.setUnit(null);
		nSoft.setOrder(3);
		nSoft.setHelpText(null);
		nSoft.setMandatory(Boolean.TRUE);
		nSoft.setAuthorizedComment(null);
		nSoft.setLabel("N° SOFT");
		nSoft.setConformity(Boolean.FALSE);
		nSoft.setPreparationCheckList(calculatorBVA);
		prepResultList.add(nSoft);

		calculatorBVA.setPreparationResultList(prepResultList);

		return calculatorBVA;
	}

	private PreparationCheckList getCalMoteurPCL(PreparationFile preparationFile) {

		PreparationCheckList calculatorMoteur = prepCheckListFactory.createPreparationCheckList();

		calculatorMoteur.setPreparationFile(preparationFile);
		calculatorMoteur.setDescription(propertyLang.getProperty("calculatormoteur.description"));
		if (Boolean.TRUE.toString().equalsIgnoreCase(propertyLang.getProperty("calculatormoteur.enable"))) {
			calculatorMoteur.setEnabled(Boolean.TRUE);
		} else {
			calculatorMoteur.setEnabled(Boolean.FALSE);
		}
		calculatorMoteur.setLabel(propertyLang.getProperty("calculatormoteur.label"));
		String var = propertyLang.getProperty("calculatormoteur.order");
		if (var != null) {
			calculatorMoteur.setOrder(Integer.parseInt(var));
		}
		calculatorMoteur.setTypeOfList(Integer.parseInt(propertyLang.getProperty("calculatormoteur.typeOfList")));

		// *************************
		List<PreparationResult> prepResultList = new ArrayList<>();

		PreparationResult marque = prepResultFactory.createPreparationResult();
		marque.setDataType(propertyLang.getProperty("calculatormoteur.marque.dataType"));
		marque.setUnit(null);

		var = propertyLang.getProperty("calculatormoteur.order");
		if (var != null) {
			marque.setOrder(Integer.parseInt(var));
		}

		marque.setHelpText(propertyLang.getProperty("calculatorbva.marque.helpText"));

		var = propertyLang.getProperty("calculatorbva.marque.mandatory");

		if (var != null && Boolean.TRUE.toString().equalsIgnoreCase(var)) {
			marque.setMandatory(Boolean.TRUE);
		}

		var = propertyLang.getProperty("calculatorbva.marque.authorizedComment");

		if (var != null && Boolean.TRUE.toString().equalsIgnoreCase(var)) {
			marque.setAuthorizedComment(Boolean.TRUE);
		}

		marque.setLabel(propertyLang.getProperty("calculatorbva.marque.label"));

		var = propertyLang.getProperty("calculatorbva.marque.confirmity");
		if (var != null && Boolean.FALSE.toString().equalsIgnoreCase(var)) {
			marque.setConformity(Boolean.FALSE);
		}

		marque.setPreparationCheckList(calculatorMoteur);
		prepResultList.add(marque);

		PreparationResult nHard = prepResultFactory.createPreparationResult();
		nHard.setDataType("String");
		nHard.setUnit(null);
		nHard.setOrder(2);
		nHard.setHelpText(null);
		nHard.setMandatory(Boolean.TRUE);
		nHard.setAuthorizedComment(null);
		nHard.setLabel("N° HARD");
		nHard.setConformity(Boolean.FALSE);
		nHard.setPreparationCheckList(calculatorMoteur);
		prepResultList.add(nHard);

		PreparationResult nSoft = prepResultFactory.createPreparationResult();
		nSoft.setDataType("String");
		nSoft.setUnit(null);
		nSoft.setOrder(3);
		nSoft.setHelpText(null);
		nSoft.setMandatory(Boolean.TRUE);
		nSoft.setAuthorizedComment(null);
		nSoft.setLabel("N° HARD");
		nSoft.setConformity(Boolean.FALSE);
		nSoft.setPreparationCheckList(calculatorMoteur);
		prepResultList.add(nSoft);

		calculatorMoteur.setPreparationResultList(prepResultList);

		return calculatorMoteur;

	}
	
	private PreparationCheckList getMotDiesel(PreparationFile preparationFile) {
		// TODO Auto-generated method stub
		return null;
	}

	private PreparationCheckList getMotGasoline(PreparationFile preparationFile) {
		// TODO Auto-generated method stub
		return null;
	}

	private PreparationCheckList getOilLevelCheckPCL(PreparationFile preparationFile) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
