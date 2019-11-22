import { Request, Response } from "express";
import { JobOffer } from "../entities/jobOffer";

// Classe que fornece as operações de interação com ofertas de emprego
class JobOfferController {
    static jobOffers: Array<JobOffer> = [];
    static uid: number = 1;

    // Operação de busca de ofertas de emprego de acordo com um conjunto de filtros fornecidos
    // Todos os campos são opcionais e fornecidos por query string
    // Se nenhum parâmetro for fornecido, o método retorna todos as ofertas de emprego cadastradas
    // 200: OK, 404: nenhuma oferta de emprego encontrada com os filtros fornecidos
    // Parâmetros de busca:
    // {
    //     id: number,
    //     area: string,
    //     companyName: string,
    //     salary: number,
    //     workload: number
    // }
    static find = async (req: Request, res: Response) => {
        console.log("/jobOffer/get: request received");

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

        console.log(`/jobOffer/get: ${filteredJobOffers.length} registers found`);

        if(filteredJobOffers.length === 0) {
            console.log("/jobOffer/get: no entries found");
            return res.status(404).send("No job offers found with required parameters");
        }

        return res.status(200).send(filteredJobOffers);
    }

    // Operação de registro de ofertas de emprego
    // Todos os campos são obrigatórios e fornecidos no formato JSON
    // Um ID único é gerado para cada oferta de emprego
    // 200: OK, 400: request malformado
    // Campos de registro:
    // {
    //     companyName: string,
    //     area: string,
    //     contact: string,
    //     salary: number,
    //     workload: number
    // }
    static register = async (req: Request, res: Response) => {
        console.log("/jobOffer/register: request received");

        const { area, companyName, contact, salary, workload } = req.body;

        if(!area || !companyName || !contact|| !salary || !workload) {
            console.log("/jobOffer/register: bad request");
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

        console.log(`/jobOffer/register: object added with id ${jobOffer.id}`);
        
        return res.status(200).send(jobOffer);
    }

    // Operação de atualização de ofertas de emprego já registradas
    // A oferta de emprego a ser atualizada é determinada por seu ID único
    // Apenas alguns campos podem ser atualizados
    // São atualizados somente os campos que forem fornecidos na request
    // Request no formato JSON
    // 200: OK, 400: request malformada, 404: nenhuma oferta de emprego encontrada para o ID fornecido
    // Campos de atualização
    // {
    //     contact: string,
    //     salary: number,
    //     workload: number
    // }
    static update = async (req: Request, res: Response) => {
        console.log("/jobOffer/update: request received");

        const { id, contact, salary, workload } = req.body;

        if(!id) {
            console.log("/jobOffer/update: bad request");
            return res.status(400).send("Bad request");
        }
        
        const filteredJobOffers: Array<JobOffer> = JobOfferController.jobOffers.filter(
            (jobOffer: JobOffer) => jobOffer.id === parseInt(id)
        );

        if(filteredJobOffers.length === 0) {
            console.log("/jobOffer/update: no curriculums found");
            return res.status(404).send("Job offer not found");
        }

        let jobOffer = filteredJobOffers[0];

        jobOffer.contact = contact || jobOffer.contact;
        jobOffer.salary = salary || jobOffer.salary;
        jobOffer.workload = workload || jobOffer.workload;

        console.log(`/jobOffer/update: object with id ${jobOffer.id} updated`);

        return res.status(200).send(jobOffer);
    }
}

export default JobOfferController;
