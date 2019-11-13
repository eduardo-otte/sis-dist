import { Request, Response } from "express";
import { JobOffer } from "../entities/jobOffer";

class JobOfferController {
    static jobOffers: Array<JobOffer> = [];
    static uid: number = 1;

    static find = async (req: Request, res: Response) => {
        const { id, area, companyName, salary, workload } = req.query;

        const filteredJobOffers: Array<JobOffer> = JobOfferController.jobOffers.filter(
            (jobOffer: JobOffer) => {
                if(id && jobOffer.id !== parseInt(id)) return false;
                if(area && jobOffer.area !== area) return false;
                if(companyName && jobOffer.companyName !== companyName) return false;
                if(salary && jobOffer.salary < salary) return false;
                if(workload && jobOffer.workload > workload) return false;

                return true;
            }
        );

        if(filteredJobOffers.length === 0) {
            return res.status(404).send("No job offers found with required parameters");
        }

        return res.status(200).send(filteredJobOffers);
    }

    static register = async (req: Request, res: Response) => {
        const { area, companyName, contact, salary, workload } = req.body;

        if(!area || !companyName || !contact|| !salary || !workload) {
            return res.status(400).send("Malformed register request");
        }

        const id = JobOfferController.uid++;

        const jobOffer: JobOffer = {
            id,
            area,
            companyName,
            contact,
            salary,
            workload,
        };

        JobOfferController.jobOffers.push(jobOffer);
        
        return res.status(200).send(jobOffer);
    }

    static update = async (req: Request, res: Response) => {
        const { id, contact, salary, workload } = req.body;

        if(!id) {
            return res.status(400).send("Bad request");
        }
        
        const filteredJobOffers: Array<JobOffer> = JobOfferController.jobOffers.filter(
            (jobOffer: JobOffer) => jobOffer.id === parseInt(id)
        );

        if(filteredJobOffers.length === 0) {
            return res.status(404).send("Job offer not found");
        }

        let jobOffer = filteredJobOffers[0];

        jobOffer.contact = contact || jobOffer.contact;
        jobOffer.salary = salary || jobOffer.salary;
        jobOffer.workload = workload || jobOffer.workload;

        return res.status(200).send(jobOffer);
    }
}

export default JobOfferController;