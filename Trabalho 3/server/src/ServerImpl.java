import models.Curriculum;
import models.JobOffering;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

public class ServerImpl implements ServerInterface {
    // jobOfferings = {"company": {"area1": [], "area2": []}}
    private HashMap<String, Curriculum> curriculumHashMap;

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

    public void registerJobOffering(JobOffering jobOffering);

    // Alter registration
    public void alterCurriculum(Curriculum newCurriculum);

    public void alterJobOffering(JobOffering newJobOffering);

    // Subscriptions
    public void subscribeForJobOfferings(ApplicantClientInterface clientRef, String areaOfInterest);

    public void subscribeForJobApplicants(CompanyClientInterface clientRef, String jobArea);
}
