import { Router } from "express";
import JobOfferController from "../controllers/jobOfferController";

const router = Router();

router.get("/get", JobOfferController.find);
router.post("/register", JobOfferController.register);
router.post("/update", JobOfferController.update);

export default router;