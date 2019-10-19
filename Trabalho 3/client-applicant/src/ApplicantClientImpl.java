import models.JobOffering;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ApplicantClientImpl extends UnicastRemoteObject implements ApplicantClientInterface {

    protected ApplicantClientImpl() throws RemoteException { }

    public void subscriptionCallback(JobOffering jobOffering){
          System.out.print("Novo Oferta de Emprego!");
          /*
            this.companyName = companyName; --Key : can't change
            this.contact = contact;
            this.area = area;               --Key : can't change
            this.workload = workload;
            this.salary = salary;
            */
          System.out.print("Nome da Empresa: " + jobOffering.getCompanyName());
          System.out.print("Contato: " + jobOffering.getContact());
          System.out.print("Area: " + jobOffering.getArea());
          System.out.print("Carga de trabalho: " + jobOffering.getWorkload());
          System.out.print("Salario pretendido: " + jobOffering.getSalary());

    }

}
