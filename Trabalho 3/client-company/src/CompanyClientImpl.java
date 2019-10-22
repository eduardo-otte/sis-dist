import models.Curriculum;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class CompanyClientImpl extends UnicastRemoteObject implements CompanyClientInterface {

    protected CompanyClientImpl() throws RemoteException { }

    public void subscriptionCallback(Curriculum curriculum) {
        System.out.println("---------------------------");
        System.out.println("\nNovo Currículo!");
        /*
        this.name = name; --Key : can't change
        this.contact = contact;
        this.area = area;
        this.workload = workload;
        this.intendedSalary = intendedSalary;
        */
        System.out.println("Nome: " + curriculum.getName());
        System.out.println("Contato: " + curriculum.getContact());
        System.out.println("Area: " + curriculum.getArea());
        System.out.println("Carga de trabalho: " + curriculum.getWorkload());
        System.out.println("Salario pretendido: " + curriculum.getIntendedSalary());
    }
}
