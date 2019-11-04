import { Request, Response } from "express";
import { Curriculum } from "../entities/curriculum";

class CurriculumController {
    static curriculums: Array<Curriculum> = [];
    static uid: number = 1;

    static find = async (req: Request, res: Response) => {
        res.status(200).send(CurriculumController.curriculums);
    }

    static register = async (req: Request, res: Response) => {
        const id = CurriculumController.uid++;

        let curriculum: Curriculum = {
            id,
            area: "Test area",
            contact: "Test contact",
            intendedSalary: 100000,
            name: "Test McTest",
            workload: 40
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
