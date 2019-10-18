import models.Curriculum;

public class CompanyClientImpl implements CompanyClientInterface {
    void subscriptionCallback(Curriculum curriculum){
          System.out.print("Novo Currículo!");
          /*
            this.name = name; --Key : can't change
            this.contact = contact;
            this.area = area;
            this.workload = workload;
            this.intendedSalary = intendedSalary;
            */
          System.out.print("Nome: " + curriculum.getName());
          System.out.print("Contato: " + curriculum.getContact());
          System.out.print("Area: " + curriculum.getArea());
          System.out.print("Carga de trabalho: " + curriculum.getWorkload());
          System.out.print("Salario pretendido: " + curriculum.getIntendedSalary());

    }

}
