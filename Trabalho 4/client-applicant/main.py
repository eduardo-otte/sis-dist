import rest_operations as ro
from types import *

url = 'http://localhost:3000'

def main():
    opt = 0
    hasCurriculum = False;
    curriculum = {'name': "MyName", 'area': "MyArea", 'contact': "MyContact", 'intendedSalary': 1000,'workload':20}

    while(opt != 4):
        print("---------------------------")
        print("Selecione a opção desejada:")
        print("1) Enviar currículo")
        print("2) Alterar dados do currículo")
        print("3) Checar vagas cadastradas")
        print("4) Encerrar")

        opt = int(input())

        if (opt == 1) and (hasCurriculum) :
            print("Você já possui um curriculo cadastrado. Se você quiser atualizar seus dados, selecione a opção 2")

        if (opt == 1) and (hasCurriculum == False) :

            print("Nome:")
            name = input();
            print("Área de Interesse:")
            area = input();
            print("Contato:")
            contact = input();
            print("Salário pretendido:")
            intendedSalary = int(input())
            print("Carga horária pretendida:")
            workload = int(input())

            curriculum['name'] = name;
            curriculum['area'] = area;
            curriculum['contact'] = contact;
            curriculum['intendedSalary'] = intendedSalary;
            curriculum['workload'] = workload;

            #print(curriculum)
            hasCurriculum = True

            response = ro.register('curriculum', curriculum)

        elif (opt == 2) and (hasCurriculum == False) :
            print("Você não possui um curriculo cadastrado. Para fazer um cadastro, selecione a opção 1")

        elif (opt == 2) and (hasCurriculum == True) :

            print("Você gostaria de atualizar seu contato?S/N")
            input_ = input()
            if input_ == 's' or input_ == 'S' :
                print("Contato:")
                name = input()
                curriculum['contact'] = name;

            print("Você gostaria de atualizar seu salário pretendido?S/N")
            input_ = input()
            if input_ == 's' or input_ == 'S' :
                print("Salário pretendido:")
                name = int(input())
                curriculum['intendedSalary'] = name;

            print("Você gostaria de atualizar sua carga horária pretendida?S/N")
            input_ = input()
            if input_ == 's' or input_ == 'S' :
                print("Carga horária pretendida:")
                name = int(input())
                curriculum['workload'] = name;

            response = ro.update('jobOffer', curriculum)
            #print(curriculum)


if __name__ == "__main__":
    main()
