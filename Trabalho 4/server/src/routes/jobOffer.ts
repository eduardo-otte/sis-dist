import { Router } from "express";

const router = Router();

router.get("/get", JobOfferController.find);

export default router;