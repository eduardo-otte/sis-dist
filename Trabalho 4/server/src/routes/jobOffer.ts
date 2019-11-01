import { Router } from "express";
import JobOfferController from "../controllers/jobOfferController";

const router = Router();

router.get("/get", JobOfferController.find);

export default router;