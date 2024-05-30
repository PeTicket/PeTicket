# PeTicket - Veterinary Clinic Application

## 1. Group Members:

| NMec | Name | email | Role |
|:---: |:---|:---|:---:|
| 108317 | Miguel Aido Miragaia         | [miguelmiragaia@ua.pt](https://github.com/Miragaia)              |    Team leader      |
| 108536 | Cristiano Antunes Nicolau    | [cristianonicolau@ua.pt](https://github.com/cristiano-nicolau)   |    DevOps Master    |
| 107323 | Vasco Miguel Faria Fernandes | [vascmfaria@ua.pt](https://github.com/vasco-faria)               |    Product Owner    |
| 97541  | Andre Lourenço Gomes         | [alg@ua.pt](https://github.com/andregomes04)                     |    QA Engineer      |

## 2. Notes:
* **Drive wiht Documentation:** [Link](https://drive.google.com/drive/folders/1TmTxcr5QHyxkNuKgjCWnforqc507J474?usp=sharing6)
 * **Jira:** [Link](https://10cccristiano.atlassian.net/jira/software/projects/SCRUM/boards/1/backlog?atlOrigin=eyJpIjoiODdiM2U3ZTI1ODRkNDRlM2I4ZDg3NDA3OGU1MWU3NDgiLCJwIjoiaiJ9)

## 3. Description:

- **Product Concept:**
    - The PeTicket application aims to provide a streamlined and modern solution for veterinary care, addressing the high-level business problem of inefficient appointment scheduling, fragmented medical record management, and suboptimal consultation management in veterinary clinics. Our system will be used to facilitate seamless appointment booking, access to comprehensive pet medical records, and efficient consultation management for both pet owners and clinic professionals.
    - Compared to other veterinary care software, PeTicket stands out with its intuitive mobile application interface, QR code check-in feature, and real-time patient flow monitoring. These features enhance convenience and efficiency, setting PeTicket apart in the market

 
- **Documentation at Github:** [Folder](https://github.com/PeTicket/PeTicket/docs)

## 4.Architecture

<p align="center">
  <img  src="./docs/architecture.png">
</p>

## 5. Repository Organization:

* **proj_ClinicFunc:** Project oriented towards the Receptionist Employee Software Service
* **proj_DS:** Project oriented towards the Display Software Service
* **proj_Pet:** Project oriented towards the Client's Pet Software Service
* **proj_Vet:** Project oriented towards the Veterinarian Software Service
* **docs:** Documentation for the project

## 6. How to run:
To run the system, _Docker Compose_ must be installed.

The steps are:

1. Compile services for the execution of _containers Docker_, running at the root of the repository:
   
    ```
    $ docker-compose build
    ```
    
2. Start the _containers_:
    
    ```
    $ docker-compose up -d
    ```
    
The **Client Web application** is available at: [localhost:8080](http://localhost:8080)

The **Vet Web application** is available at: [localhost:8081](http://localhost:8081)

The **Funcionary Web application** is available at: [localhost:8082](http://localhost:8082)

The **Display Web application** is available at: [localhost:8083](http://localhost:8083)

It is also deployed in an UA Server, available using the UA network or the UA VPN at:
- Client -> [Client_Web](mednati.ieeta...)
- Vet -> [Vet_Web](mednati.ieeta...)
- Funcionary -> [Func_Web](mednati.ieeta...)
- Display -> [Display_Web](mednati.ieeta...)

## 7.Project Bookmarks

### [**Code Quality Dashboard (SonarCloud)**](https://sonarcloud.io/organizations/peticket/projects) 

**Client Credentials**
Login | user@email.com |
--- | --- |
Password | password |

**Vet Credentials**
Login | vet@email.com |
--- | --- |
Password | password |

**Func Credentials**
Login | func@email.com |
--- | --- |
Password | password |

### [**TQS Delivery REST API Docs**](link aqui) 
