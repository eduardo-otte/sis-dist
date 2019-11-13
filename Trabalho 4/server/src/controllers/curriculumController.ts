import { Request, Response } from "express";
import { Curriculum } from "../entities/curriculum";

class CurriculumController {
    static curriculums: Array<Curriculum> = [];
    static uid: number = 1;

    static find = async (req: Request, res: Response) => {
        console.log("/curriculum/get: request received");
        
        const { id, area, name, intendedSalary, workload } = req.query;

        const filteredCurriculums = CurriculumController.curriculums.filter(
            (curriculum: Curriculum) => {
                if(id && curriculum.id !== parseInt(id)) return false;
                if(area && curriculum.area !== area) return false;
                if(name && curriculum.name !== name) return false;
                if(intendedSalary && curriculum.intendedSalary > intendedSalary) return false;
                if(workload && curriculum.workload < workload) return false;

                return true;
            }
        );

        console.log(`/curriculum/get: ${filteredCurriculums.length} registers found`);

        if(filteredCurriculums.length === 0) {
            console.log("/curriculum/get: no entries found");
            return res.status(404).send("No curriculums found with given parameters");
        }

        return res.status(200).send(filteredCurriculums);
    }

    static register = async (req: Request, res: Response) => {
        console.log("/curriculum/register: request received");

        const { name, area, contact, intendedSalary, workload } = req.body;

        if(!name || !area || !contact || !intendedSalary || !workload) {
            console.log("/curriculum/register: bad request");
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

        console.log(`/curriculum/register: object added with id ${curriculum.id}`);

        return res.status(200).send(curriculum);
    }

    static update = async (req: Request, res: Response) => {
        console.log("/curriculum/update: request received");

        const { id, contact, intendedSalary, workload } = req.body;

        if(!id) {
            console.log("/curriculum/update: bad request");
            return res.status(400).send("Bad request");
        }

        const filteredCurriculums: Array<Curriculum> = CurriculumController.curriculums.filter(
            (curriculum: Curriculum) => curriculum.id === parseInt(id)
        );

        if(filteredCurriculums.length === 0) {
            console.log("/curriculum/update: no curriculums found");
            return res.status(404).send("Curriculum not found");
        }

        let curriculum = filteredCurriculums[0];

        curriculum.contact = contact || curriculum.contact;
        curriculum.intendedSalary = intendedSalary || curriculum.intendedSalary;
        curriculum.workload = workload || curriculum.workload;

        console.log(`/curriculum/update: object with id ${curriculum.id} updated`);

        return res.status(200).send(curriculum);
    }
}

export default CurriculumController;
