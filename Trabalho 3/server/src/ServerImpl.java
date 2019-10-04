import models.Curriculum;
import models.JobOffering;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

public class ServerImpl implements ServerInterface {
    // jobOfferings = {"company": {"area1": [], "area2": []}}
    private HashMap<String, Curriculum> curriculumHashMap;
    private HashMap<String, JobOffering> jobOfferingHashMap;

    // Search
    public ArrayList<JobOffering> searchJobOffering(JobOffering jobOfferingLookup);

    public ArrayList<Curriculum> searchCurriculum(String areaOfInterest);

    // Registration
    public void registerCurriculum(Curriculum curriculum) {
        if(curriculumHashMap.containsKey(curriculum.getName())) {
            // return error
        }

        curriculumHashMap.put(curriculum.getName(), curriculum);
        // return success
    }
    /*key for jobOfferingHashMap is "[companyName]-[area]" */
    public void registerJobOffering(JobOffering jobOffering){
      string key = jobOffering.getCompanyName()+"-"+jobOffering.getArea();

      if(jobOfferingHashMap.containsKey(key)) {
          // return error
      }

      jobOfferingHashMap.put(key, jobOffering);
      // return success

    }

    // Alter registration
    public void alterCurriculum(Curriculum newCurriculum){
        if(!curriculumHashMap.containsKey(curriculum.getName())) {
            // return error - doesnt have this person's curriculum in the database
        }

        /*
        this.name = name; --Key : cant change
        this.contact = contact;
        this.area = area;
        this.workload = workload;
        this.intendedSalary = intendedSalary;
        */

        /*
          I don't know if we'll send just what its gonna change, so....
        */
        Curriculum previousCurriculum = curriculumHashMap.get(curriculum.getName());
        //if(newCurriculum.getcontact()!=null)
        previousCurriculum.setContact(newCurriculum.getcontact());
        //if(newCurriculum.getArea()!=null)
        previousCurriculum.setArea(newCurriculum.getArea());
        //if(newCurriculum.getWorkload()!=null)
        previousCurriculum.setWorkload(newCurriculum.getWorkload());
        //if(newCurriculum.getIntendedSalary()!=null)
        previousCurriculum.setIntendedSalary(newCurriculum.getIntendedSalary());

        // return success
    }

    public void alterJobOffering(JobOffering newJobOffering){
      string key = newJobOffering.getCompanyName()+"-"+newJobOffering.getArea();
      if(!jobOfferingHashMap.containsKey(key)) {
          // return error- doesnt have this company's Job offer
      }
      /*
      this.companyName = companyName; --Key : cant change
      this.contact = contact;
      this.area = area;               --Key : cant change
      this.workload = workload;
      this.salary = salary;
      */

      /*
        I don't know if we'll send just what its gonna change, so....
      */
      JobOffering previousJobOffer = jobOfferingHashMap.get(key);
      //if(newJobOffering.getContact()!=null)
      previousJobOffer.setContact() = newJobOffering.getContact();
      //if(newJobOffering.getWorkload()!=null)
      previousJobOffer.setWorkload() = newJobOffering.getWorkload();
      //if(newJobOffering.getSalary()!=null)
      previousJobOffer.setSalary() = newJobOffering.getSalary();

    }

    // Subscriptions
    public void subscribeForJobOfferings(ApplicantClientInterface clientRef, String areaOfInterest);

    public void subscribeForJobApplicants(CompanyClientInterface clientRef, String jobArea);
}
