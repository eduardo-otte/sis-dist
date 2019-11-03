import { Request, Response } from "express";
import { JobOffer } from "../entities/jobOffer";

interface JobOfferCollection {
    [key: string]: any;
}

class JobOfferController {
    static jobOffers: JobOfferCollection = {};
    static uid: number = 0;

    static find = async (req: Request, res: Response) => {
        return res.status(200).send(JobOfferController.jobOffers);
    }

    static register = async (req: Request, res: Response) => {
        const id = JobOfferController.uid++;

        const jobOffer: JobOffer = {
            id,
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

        if(!JobOfferController.jobOffers[area][companyName]) {
            JobOfferController.jobOffers[area][companyName] = [];
        }

        JobOfferController.jobOffers[area][companyName].push(jobOffer);
        return res.status(200).send("Job offer registered successfully");
    }

    static update = async (req: Request, res: Response) => {
        const { id, area, companyName, contact, salary, workload } = req.body;

        if(!area || !companyName || !id) {
            return res.status(400).send("Bad request");
        }

        if(!JobOfferController.jobOffers[area]) {
            return res.status(404).send("Area not found");
        }

        if(!JobOfferController.jobOffers[area][companyName]) {
            return res.status(404).send("Company not found");
        }

        const filteredJobOffers: Array<JobOffer> = JobOfferController.jobOffers.filter((jobOffer: JobOffer) => jobOffer.id === id);

        if(filteredJobOffers.length === 0) {
            return res.status(404).send("Job offer not found");
        }

        let jobOffer = filteredJobOffers[0];

        jobOffer.contact = contact || jobOffer.contact;

        jobOffer.salary = salary || jobOffer.salary;

        jobOffer.workload = workload || jobOffer.workload;

        return res.status(200).send("Job offer updated successfully");
    }
}

export default JobOfferController;