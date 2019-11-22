import { Router, Request, Response } from "express";
import curriculum from "./curriculum";
import jobOffer from "./jobOffer";

const routes = Router();

// Associa a operação de cada entidade a suas respectivas rotas
routes.use("/curriculum", curriculum);
routes.use("/jobOffer", jobOffer);

export default routes;