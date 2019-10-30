import models.JobOffering;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ApplicantClientImpl extends UnicastRemoteObject implements ApplicantClientInterface {

    protected ApplicantClientImpl() throws RemoteException { }

    /**
     * Recebimento de notificação do servidor quando uma nova oferta de emprego é cadastrada na área de interesse desejada
     * @param jobOffering Nova oferta de emprego
     */
    public void subscriptionCallback(JobOffering jobOffering) {
        System.out.println("---------------------------");
        System.out.println("Novo Oferta de Emprego!");
        /*
        this.companyName = companyName; --Key : can't change
        this.contact = contact;
        this.area = area;               --Key : can't change
        this.workload = workload;
        this.salary = salary;
        */
        System.out.println("Nome da Empresa: " + jobOffering.getCompanyName());
        System.out.println("Contato: " + jobOffering.getContact());
        System.out.println("Area: " + jobOffering.getArea());
        System.out.println("Carga de trabalho: " + jobOffering.getWorkload());
        System.out.println("Salario pretendido: " + jobOffering.getSalary());
    }

}
