# Use node image for development
FROM node:16-alpine

WORKDIR /app

# Install dependencies
COPY package*.json ./
RUN npm install

# Copy the rest of the files
COPY . .

# Expose port 3000 and start the app
EXPOSE 3000
CMD ["npm", "start"]
