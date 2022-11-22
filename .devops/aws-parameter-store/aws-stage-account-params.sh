echo Started adding application properties required for AWS Stage Account
echo shell used  = "$SHELL"
./spring-profile/application.sh
./spring-profile/application-stage.sh
echo Completed adding application properties required for AWS Stage Account
