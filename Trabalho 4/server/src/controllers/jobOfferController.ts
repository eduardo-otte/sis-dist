import { Request, Response } from "express";
import { JobOffer } from "../entities/jobOffer";

class JobOfferController {
    static find = async (req: Request, res: Response) => {
        const jobOffer: JobOffer = {
            area: "Test area",
            companyName: "Test & Testers Inc.",
            contact: "Test contact",
            salary: 100000,
            workload: 10000
        }

        return res.status(200).send(jobOffer);
    }
}

export default JobOfferController;