# ---- Build Stage ----
# Use an official Node.js image with a version that meets your requirements
FROM node:22-alpine AS builder

# Set the working directory
WORKDIR /app

# Copy package files AND your Svelte config file
COPY package.json package-lock.json svelte.config.js ./

# ---- THIS IS THE KEY CHANGE ----
# Temporarily set NODE_ENV to 'development' to ensure devDependencies are installed
# Then, install dependencies with `npm ci`
RUN NODE_ENV=development npm ci

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
