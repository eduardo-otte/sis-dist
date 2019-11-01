import { Request, Response } from "express";
import { Curriculum } from "../entities/curriculum";

interface CurriculumCollection {
    [key: string]: any;
}

class CurriculumController {
    static curriculums: CurriculumCollection = {};

    static find = async (req: Request, res: Response) => {
        res.status(200).send(CurriculumController.curriculums);
    }

    static register = async (req: Request, res: Response) => {
        let curriculum: Curriculum = {
            area: "Test area",
            contact: "Test contact",
            intendedSalary: 100000,
            name: "Test McTest",
            workload: 40
        };

        const { name, area } = curriculum;

        if(!CurriculumController.curriculums[area]) {
            CurriculumController.curriculums[area] = {};
        }

        CurriculumController.curriculums[area][name] = curriculum;
        return res.status(200).send("Curriculum registered successfully");
    }

    static update = async (req: Request, res: Response) => {
        const { area, contact, intendedSalary, name, workload } = req.body;

        if(!area || !name) {
            return res.status(400).send("Bad request");
        }

        if(!CurriculumController.curriculums[area][name]) {
            return res.status(404).send("Curriculum not found");
        }

        let curriculum = CurriculumController.curriculums[area][name];

        if(contact) {
            curriculum.contact = contact;
        }

        if(intendedSalary) {
            curriculum.intendedSalary = intendedSalary;
        }

        if(workload) {
            curriculum.workload = workload;
        }

        return res.status(200).send("Curriculum updated successfully");
    }
}

export default CurriculumController;
