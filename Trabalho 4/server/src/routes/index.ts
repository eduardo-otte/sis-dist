import { Router, Request, Response } from "express";
import curriculum from "./curriculum";
import jobOffer from "./jobOffer";

const routes = Router();

routes.use("/curriculum", curriculum);
routes.use("/jobOffer", jobOffer);

export default routes;