import models.Curriculum;
import models.JobOffering;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class ServerImpl implements ServerInterface {
    private HashMap<String, Curriculum> curriculumHashMap;
    private HashMap<String, JobOffering> jobOfferingHashMap;
    private HashMap<ApplicantClientInterface, String> subscribedClients;
    private HashMap<CompanyClientInterface, String> subscribedCompanies;

    // Search
    public ArrayList<JobOffering> searchJobOffering(JobOffering jobOfferingLookup){
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
    public void registerCurriculum(Curriculum curriculum) {
        if(!curriculumHashMap.containsKey(curriculum.getName())) {
        	curriculumHashMap.put(curriculum.getName(), curriculum);
        }
    }

    /* Key for jobOfferingHashMap is "[companyName]-[area]"
       Move into JavaDoc later */
    public void registerJobOffering(JobOffering jobOffering){
      String key = jobOffering.getCompanyName()+"-"+jobOffering.getArea();

      if(!jobOfferingHashMap.containsKey(key)) {
    	  jobOfferingHashMap.put(key, jobOffering);
      }
    }

    // Alter registration
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
    public void subscribeForJobOfferings(ApplicantClientInterface clientRef, String areaOfInterest) {
    	if(!subscribedClients.containsKey(clientRef)) {
    		subscribedClients.put(clientRef, areaOfInterest);
    	}
    }

    public void subscribeForJobApplicants(CompanyClientInterface clientRef, String jobArea){
    	if(!subscribedCompanies.containsKey(clientRef)) {
    		subscribedCompanies.put(clientRef, jobArea);
    	}
    }
  
}
