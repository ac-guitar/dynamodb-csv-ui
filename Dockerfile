FROM java:8-jdk-alpine
COPY . /app
CMD java -jar /app/dynamodb-csv-ui-1.0.jar