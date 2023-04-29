# Use Ubuntu as the base image
FROM ubuntu:22.04

# Update packages and install Python3, pip, and MySQL Connector dependencies
RUN apt-get update && \
    apt-get install -y python3 python3-pip libmysqlclient-dev && \
    rm -rf /var/lib/apt/lists/*

# Set the working directory to /app
WORKDIR /app

# Copy the requirements file to the container
COPY requirements.txt .

# Install the Python dependencies
RUN pip3 install --no-cache-dir -r requirements.txt

# Copy the rest of the application code to the container
COPY . .

# Set the default command to run the Flask app
CMD ["python3", "main.py"]