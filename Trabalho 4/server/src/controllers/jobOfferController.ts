import { Request, Response } from "express";
import { JobOffer } from "../entities/jobOffer";

interface JobOfferCollection {
    [key: string]: any;
}

class JobOfferController {
    static jobOffers: JobOfferCollection = {};

    static find = async (req: Request, res: Response) => {
        return res.status(200).send(JobOfferController.jobOffers);
    }

    static register = async (req: Request, res: Response) => {
        const jobOffer: JobOffer = {
            area: "Test area",
            companyName: "Test & Testers Inc.",
            contact: "Test contact",
            salary: 100000,
            workload: 10000
        };

        const { companyName, area } = jobOffer;

        if(!JobOfferController.jobOffers[area]) {
            JobOfferController.jobOffers[area] = {};
        }

        JobOfferController.jobOffers[area][companyName] = jobOffer;
        return res.status(200).send();
    }
}

export default JobOfferController;