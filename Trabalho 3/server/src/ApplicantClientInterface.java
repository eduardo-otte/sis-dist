import models.JobOffering;

import java.util.ArrayList;

public interface ApplicantClientInterface {
    void subscriptionCallback(ArrayList<JobOffering> results);
}
