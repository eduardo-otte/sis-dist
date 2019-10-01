import models.Curriculum;
import models.JobOffering;

import java.util.ArrayList;

public interface ServerInterface {
    // Search
    ArrayList<JobOffering> searchJobOffering(JobOffering jobOfferingLookup);

    ArrayList<Curriculum> searchCurriculum(String areaOfInterest);

    // Registration
    void registerCurriculum(Curriculum curriculum);

    void registerJobOffering(JobOffering jobOffering);

    // Alter registration
    void alterCurriculum(Curriculum newCurriculum);

    void alterJobOffering(JobOffering newJobOffering);

    // Subscriptions
    void subscribeForJobOfferings(ApplicantClientInterface clientRef, String areaOfInterest);

    void subscribeForJobApplicants(CompanyClientInterface clientRef, String jobArea);
}
