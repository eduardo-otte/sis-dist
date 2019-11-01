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
        return res.status(200).send();
    }
}

export default CurriculumController;
