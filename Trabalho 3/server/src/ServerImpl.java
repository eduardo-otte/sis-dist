import models.Curriculum;
import models.JobOffering;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class ServerImpl extends UnicastRemoteObject implements ServerInterface {
    private HashMap<String, Curriculum> curriculumHashMap = new HashMap<>();
    private HashMap<String, JobOffering> jobOfferingHashMap = new HashMap<>();

    private HashMap<String, ArrayList<ApplicantClientInterface>> subscribedApplicants = new HashMap<>();
    private HashMap<String, ArrayList<CompanyClientInterface>> subscribedCompanies = new HashMap<>();

    protected ServerImpl() throws RemoteException { }

    // Search

    /**
     * Procura de empregos ofertados que coincidam com os parâmetros pretendidos
     * @param jobOfferingLookup: Parâmetros para a procura de emprego
     * @return: Lista de empregos que coincidam com os parâmetros passados
     */
    public ArrayList<JobOffering> searchJobOffering(JobOffering jobOfferingLookup) {
        ArrayList<JobOffering> jobsFound = new ArrayList<>();
        
        for (Entry<String, JobOffering> jobEntry : jobOfferingHashMap.entrySet()) {
            JobOffering jobOffering = jobEntry.getValue();
            if(jobOffering.getArea().equalsIgnoreCase(jobOfferingLookup.getArea()) &&
            	jobOffering.getSalary() >= jobOfferingLookup.getSalary()) {
            	jobsFound.add(jobOffering);
            }
        }
        return jobsFound;
	}

    /**
     * Procura de currículos que coincidam com a área pretendida
     * @param areaOfInterest: Área de interesse a ser procurada
     * @return: Lista de currículos que coincidam com a Área pretendida
     */
    public ArrayList<Curriculum> searchCurriculum(String areaOfInterest){
        ArrayList<Curriculum> curriculumFound = new ArrayList<>();
        
        for (Entry<String, Curriculum> curriculumEntry : curriculumHashMap.entrySet()) {
        	Curriculum curriculum = curriculumEntry.getValue();
            if(curriculum.getArea().equalsIgnoreCase(areaOfInterest)) {
            	curriculumFound.add(curriculum);
            }
        }
        return curriculumFound;
    }

    // Registration

    /**
     * Registro de um novo currículo no sistema
     * @param curriculum: Currículo a ser cadastrado
     */
    public void registerCurriculum(Curriculum curriculum) {
        if(!curriculumHashMap.containsKey(curriculum.getName())) {
        	curriculumHashMap.put(curriculum.getName(), curriculum);

        	System.out.println("Novo currículo adicionado");
        	System.out.println("Nome: " + curriculum.getName());
            System.out.println("Área: " + curriculum.getArea());
            System.out.println("----------------");

        	if(subscribedCompanies.containsKey(curriculum.getArea())) {
        	    for(CompanyClientInterface company : subscribedCompanies.get(curriculum.getArea())) {
        	        try {
                        company.subscriptionCallback(curriculum);
                    } catch (Exception e) {
        	            System.out.println("Exceção ao notificar cliente: " + e.getMessage());
                    }
                }
            }
        }
    }

    /* Key for jobOfferingHashMap is "[companyName]-[area]"
       Move into JavaDoc later */

    /**
     * Registro de uma nova vaga de emprego no sistema
     * @param jobOffering: Vaga de emprego a ser registrada
     */
    public void registerJobOffering(JobOffering jobOffering){
        String key = jobOffering.getCompanyName() + "-" + jobOffering.getArea();

        if(!jobOfferingHashMap.containsKey(key)) {
            jobOfferingHashMap.put(key, jobOffering);
            System.out.println("Nova oferta de emprego registrada");
            System.out.println("Companhia: " + jobOffering.getCompanyName());
            System.out.println("Área: " + jobOffering.getArea());
            System.out.println("----------------");

            if(subscribedApplicants.containsKey(jobOffering.getArea())) {
                for(ApplicantClientInterface applicant : subscribedApplicants.get(jobOffering.getArea())) {
                    try {
                        System.out.println("\tEnviando notificação para um cliente");
                        applicant.subscriptionCallback(jobOffering);
                    } catch (Exception e) {
                        System.out.println("Exceção ao tentar notificar a companhia: " + e.getMessage());
                    }
                }
            }
        }
    }

    /**
     * Alteração de um currículo
     * @param newCurriculum: Currículo com novos dados para ser atualizado no sistema
     */
    public void alterCurriculum(Curriculum newCurriculum){
    	/*
        this.name = name; --Key : can't change
        this.contact = contact;
        this.area = area;
        this.workload = workload;
        this.intendedSalary = intendedSalary;
        */
    	
    	if(curriculumHashMap.containsKey(newCurriculum.getName())) {
			Curriculum previousCurriculum = curriculumHashMap.get(newCurriculum.getName());
			  
			if(newCurriculum.getContact() != null)
			    previousCurriculum.setContact(newCurriculum.getContact());
			if(newCurriculum.getArea() != null)
			    previousCurriculum.setArea(newCurriculum.getArea());
			if(newCurriculum.getWorkload() > 0)
			    previousCurriculum.setWorkload(newCurriculum.getWorkload());
			if(newCurriculum.getIntendedSalary() > 0.0)
			    previousCurriculum.setIntendedSalary(newCurriculum.getIntendedSalary());
		}
    }

    /**
     * Alterçao de emprego ofertgado
     * @param newJobOffering: Emprego ofertado com novos dados para ser atualizados no sistema
     */
    public void alterJobOffering(JobOffering newJobOffering){
    	/*
        this.companyName = companyName; --Key : can't change
        this.contact = contact;
        this.area = area;               --Key : can't change
        this.workload = workload;
        this.salary = salary;
        */
		
    	String key = newJobOffering.getCompanyName() + "-" + newJobOffering.getArea();

    	if(jobOfferingHashMap.containsKey(key)) {
    		JobOffering previousJobOffer = jobOfferingHashMap.get(key);
    		if(newJobOffering.getContact() != null)
    		    previousJobOffer.setContact(newJobOffering.getContact());
    		if(newJobOffering.getWorkload() > 0)
    		    previousJobOffer.setWorkload(newJobOffering.getWorkload());
    		if(newJobOffering.getSalary() > 0.0)
    		    previousJobOffer.setSalary(newJobOffering.getSalary());
    	}	  
	}

    // Subscriptions

    /**
     * Cadastro do "cliente aplicante" para recebimento de notificações de oferta de emprego
     * @param clientRef: Referência do cliente
     * @param areaOfInterest: Área de interesse do cliente
     */
    public void subscribeForJobOfferings(ApplicantClientInterface clientRef, String areaOfInterest) {
        if(!subscribedApplicants.containsKey(areaOfInterest)) {
            subscribedApplicants.put(areaOfInterest, new ArrayList<>());
        }

        if(!subscribedApplicants.get(areaOfInterest).contains(clientRef)) {
            subscribedApplicants.get(areaOfInterest).add(clientRef);
        }
    }

    /**
     * Cadastro do "cliente companhia" para recebimento de notificações de currículos
     * @param clientRef: Referência do cliente
     * @param jobArea: Área de interesse do cliente
     */
    public void subscribeForJobApplicants(CompanyClientInterface clientRef, String jobArea){
        if(!subscribedCompanies.containsKey(jobArea)) {
            subscribedCompanies.put(jobArea, new ArrayList<>());
        }

        if(!subscribedCompanies.get(jobArea).contains(clientRef)) {
            subscribedCompanies.get(jobArea).add(clientRef);
        }
    }
}
