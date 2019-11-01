import { Router } from "express";
import CurriculumController from "../controllers/curriculumController";

const router = Router();

router.get("/get", CurriculumController.find);
router.post("/register", CurriculumController.register);
router.post("/update", CurriculumController.update);

export default router;