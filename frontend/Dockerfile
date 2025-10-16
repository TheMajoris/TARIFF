# ---- Build Stage ----
# Use an official Node.js image with a version that meets your requirements
FROM node:22-alpine AS builder

# Set the working directory
WORKDIR /app

# Copy package files AND your Svelte config file
# This ensures that if the config changes, dependencies are re-installed
COPY package.json package-lock.json svelte.config.js ./

# Install dependencies
# This command will now re-run if svelte.config.js has changed
RUN npm ci

# Copy the rest of the application code
COPY . .

# Build the application
RUN npm run build

# ---- Production Stage ----
# Use a lightweight web server to serve the static files
FROM nginx:stable-alpine

# Copy the built files from the "builder" stage
# Make sure this points to the correct output directory ('build')
COPY --from=builder /app/build /usr/share/nginx/html

# Expose port 80
EXPOSE 80

# The default Nginx command will start the server
CMD ["nginx", "-g", "daemon off;"]
