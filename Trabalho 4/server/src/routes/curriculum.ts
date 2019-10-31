import { Router } from "express";

const router = Router();

router.get("/get", CurriculimController.find);

export default router;