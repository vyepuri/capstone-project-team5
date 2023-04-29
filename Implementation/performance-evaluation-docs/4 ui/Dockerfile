# Use Ubuntu as the base image
FROM ubuntu:22.04

# Install Apache2 and update packages
RUN apt-get update && \
    apt-get install -y apache2

WORKDIR /var/www/html/
COPY *.html ./
COPY css css
COPY js js
COPY properties properties
EXPOSE 80

# Start Apache2 service
CMD ["apachectl", "-D", "FOREGROUND"]