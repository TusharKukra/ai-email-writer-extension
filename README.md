# Screenshots :

## 1. Google Gemini API Dashboard
<img width="1915" height="755" alt="Google_Gemini_API_Dashboard" src="https://github.com/user-attachments/assets/65568463-c43e-4bc1-a234-dd3ac8745fb9" />

## 2. AI Reply button integrated with Gmail UI
<img width="1910" height="819" alt="Gmail_AIReply_Button_Overview" src="https://github.com/user-attachments/assets/92834595-a0c2-4289-be3a-439deede6ab4" />

## 3. After clicking AI Reply, generating AI response
<img width="1918" height="808" alt="Generating_AI_Response_Button" src="https://github.com/user-attachments/assets/42790f6a-097b-4fae-8892-8d46dea41206" />

## 4. Response 
<img width="1915" height="817" alt="Gmail_Response" src="https://github.com/user-attachments/assets/8ec275db-1c1e-47a9-ad5a-43c7d60f1c87" />

## 5. Can also check the API using Postman
<img width="1277" height="861" alt="API_Postman_Response" src="https://github.com/user-attachments/assets/893ca8e8-4aab-4c90-a9ac-5d6d8295c901" />

## 6. Google Chrome Extension
<img width="1906" height="672" alt="image" src="https://github.com/user-attachments/assets/d903836a-23ce-4de0-b413-378e212d7606" />


## AWS EC2 Deployment
### 1. Create a EC2 Instance
### 2. Create JAR file of spring boot application
### 3. Logging in AWS EC2 using certificate & SSH command
### 4. Paste JAR to home/ec2-user folder in EC2 from your local
### 5. Using CLI, check whether your EC2 instance has JAVA installed or not
### 6. If not then run : sudo yum update -y , after that install java 17 : sudo yum install java-17-amazon-corretto-devel -y



## 🧱 AWS EC2 Deployment Steps

### 1️⃣ Create an EC2 Instance

1. Log in to **AWS Management Console**
2. Go to **EC2 → Launch Instance**
3. Select **Amazon Linux 2**
4. Choose instance type (e.g. `t2.micro`)
5. Create or select an existing **Key Pair (.pem)**
6. Configure **Security Group**:
   - Allow **SSH (22)**
   - Allow **Custom TCP (8080)**
7. Launch the instance

---

## 2️⃣ Create JAR File of Spring Boot Application

From the root directory of the Spring Boot project:

```bash
mvn clean package
```

## 3️⃣ Upload JAR File to EC2
```bash
scp -i <your-key.pem> target/email-writer-backend-0.0.1-SNAPSHOT.jar ec2-user@<public-ip>:/home/ec2-user/
```

## 4️⃣ Login to EC2 Using SSH
Linux / macOS
```bash
ssh -i <your-key.pem> ec2-user@<public-ip>
```

## 5️⃣ Install Java 17 on EC2 (Amazon Linux 2)

Update system packages:
```bash
sudo yum update -y
```
Install Java 17:
```bash
sudo yum install java-17-amazon-corretto -y
```
Verify installation:
```bash
java -version
```

## 6️⃣ Run Spring Boot Application Using nohup

Run the application in the background so it continues running after logout:
```bash
nohup java -jar email-writer-backend-0.0.1-SNAPSHOT.jar > output.log 2>&1 &
```

<p>
Command Explanation:

nohup
Prevents the application from stopping when the SSH session is closed or the user logs out of the EC2 instance.

java -jar email-writer-backend-0.0.1-SNAPSHOT.jar
Starts the Spring Boot application from the executable JAR file.

> output.log
Redirects standard output (application logs) to the file output.log.

2>&1
Redirects standard error output (error logs) to the same file as standard output.

&
Runs the application as a background process, allowing the terminal to be used for other commands.
</p>

## 7️⃣ View Application Logs

To monitor logs in real time:
```bash
tail -f output.log
```
