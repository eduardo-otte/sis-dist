import { Request, Response } from "express";
import { Curriculum } from "../entities/curriculum";

class CurriculumController {
    static curriculums: Array<Curriculum> = [];
    static uid: number = 1;

    static find = async (req: Request, res: Response) => {
        const { id, area, name, intendedSalary, workload } = req.query;

        const filteredCurriculums = CurriculumController.curriculums.filter(
            (curriculum: Curriculum) => {
                if(id && curriculum.id !== id) return false;
                if(area && curriculum.area !== area) return false;
                if(name && curriculum.name !== name) return false;
                if(intendedSalary && curriculum.intendedSalary > intendedSalary) return false;
                if(workload && curriculum.workload < workload) return false;

                return true;
            }
        );

        if(filteredCurriculums.length === 0) {
            return res.status(404).send("No curriculums found with given parameters");
        }

        return res.status(200).send(filteredCurriculums);
    }

    static register = async (req: Request, res: Response) => {
        const { name, area, contact, intendedSalary, workload } = req.body;

        if(!name || !area || !contact || !intendedSalary || !workload) {
            return res.status(400).send("Malformed register request");
        }

        const id = CurriculumController.uid++;

        let curriculum: Curriculum = {
            id,
            name,
            area,
            contact,
            intendedSalary,
            workload,
        };

        CurriculumController.curriculums.push(curriculum);

        return res.status(200).send("Curriculum registered successfully");
    }

    static update = async (req: Request, res: Response) => {
        const { id, area, contact, intendedSalary, name, workload } = req.body;

        if(!id) {
            return res.status(400).send("Bad request");
        }

        const filteredCurriculums: Array<Curriculum> = CurriculumController.curriculums.filter(
            (curriculum: Curriculum) => curriculum.id === id
        );

        if(filteredCurriculums.length === 0) {
            return res.status(404).send("Curriculum not found");
        }

        let curriculum = filteredCurriculums[0];

        curriculum.contact = contact || curriculum.contact;
        curriculum.intendedSalary = intendedSalary || curriculum.intendedSalary;
        curriculum.workload = workload || curriculum.workload;

        return res.status(200).send("Curriculum updated successfully");
    }
}

export default CurriculumController;
