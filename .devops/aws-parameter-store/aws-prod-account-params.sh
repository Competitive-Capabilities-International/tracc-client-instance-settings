echo Started adding application properties required for AWS Prod Account
echo shell used  = "$SHELL"
./spring-profile/application.sh
./spring-profile/application-prod.sh
echo Completed adding application properties required for AWS Prod Account
