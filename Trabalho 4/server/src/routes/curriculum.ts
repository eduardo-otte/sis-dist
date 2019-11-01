import { Router } from "express";
import CurriculumController from "../controllers/curriculumController";

const router = Router();

router.get("/get", CurriculumController.find);

export default router;