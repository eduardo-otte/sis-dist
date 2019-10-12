import models.Curriculum;
import models.JobOffering;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class ServerImpl implements ServerInterface {
    // jobOfferings = {"company": {"area1": [], "area2": []}}
    private HashMap<String, Curriculum> curriculumHashMap;
    private HashMap<String, JobOffering> jobOfferingHashMap;
    private HashMap<ApplicantClientInterface,String> subscribedClients;
    private HashMap<CompanyClientInterface,String> subscribedCompanies;

    // Search
    public ArrayList<JobOffering> searchJobOffering(JobOffering jobOfferingLookup){
    	//filtros : área e interesse e salário mínimo pretendido

        ArrayList<JobOffering> jobsFound = new ArrayList<JobOffering>();
        
        for (Entry<String, JobOffering> job_: jobOfferingHashMap.entrySet()) {
            JobOffering job = job_.getValue();
            if(job.getArea().equalsIgnoreCase(jobOfferingLookup.getArea()) &&
            	job.getSalary() >= jobOfferingLookup.getSalary()) {
            	jobsFound.add(job);
            }
        }
        return jobsFound;
	}

    public ArrayList<Curriculum> searchCurriculum(String areaOfInterest){
    	//filtros : área e interesse 
    	
        ArrayList<Curriculum> curriculumFound = new ArrayList<Curriculum>();
        
        for (Entry<String, Curriculum> curriculum_: curriculumHashMap.entrySet()) {
        	Curriculum curriculum = curriculum_.getValue();
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
    /*key for jobOfferingHashMap is "[companyName]-[area]" */
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
			//I don't know if we'll send just what its gonna change, so....
			
			Curriculum previousCurriculum = curriculumHashMap.get(newCurriculum.getName());
			  
			//if(newCurriculum.getcontact()!=null)
			previousCurriculum.setContact(newCurriculum.getContact());
			//if(newCurriculum.getArea()!=null)
			previousCurriculum.setArea(newCurriculum.getArea());
			//if(newCurriculum.getWorkload()!=null)
			previousCurriculum.setWorkload(newCurriculum.getWorkload());
			//if(newCurriculum.getIntendedSalary()!=null)
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
		
    	String key = newJobOffering.getCompanyName()+"-"+newJobOffering.getArea();
    	
    	if(jobOfferingHashMap.containsKey(key)) {
    		JobOffering previousJobOffer = jobOfferingHashMap.get(key);
    		//if(newJobOffering.getContact()!=null)
    		previousJobOffer.setContact(newJobOffering.getContact());
    		//if(newJobOffering.getWorkload()!=null)
    		previousJobOffer.setWorkload(newJobOffering.getWorkload());
    		//if(newJobOffering.getSalary()!=null)
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
