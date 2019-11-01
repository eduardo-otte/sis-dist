import { Request, Response } from "express";
import { Curriculum } from "../entities/curriculum";

class CurriculumController {
    static find = async (req: Request, res: Response) => {
        // find a curriculum
        let curriculum: Curriculum = {
            area: "Test area",
            contact: "Test contact",
            intendedSalary: 100000,
            name: "Test McTest",
            workload: 40
        };

        res.status(200).send(curriculum);
    }
}

export default CurriculumController;
