import { execSync } from 'child_process';
import path from 'path';
import { fileURLToPath } from 'url';

export default async () => {
    console.log('Starting backend Docker container...');
    try {
        // Example using docker-compose
        const __filename = fileURLToPath(import.meta.url);
        const __dirname = path.dirname(__filename);
        const coreDir = path.resolve(__dirname, '../../core');
        execSync('docker compose down -v', {
            cwd: coreDir,        // run inside /core directory
            stdio: 'inherit',    // print Docker logs directly
        });

        execSync('docker compose up --build -d', {
            cwd: coreDir,        // run inside /core directory
            stdio: 'inherit',    // print Docker logs directly
        });

        const BACKEND_URL = "http://localhost:8080"
        const MAX_ATTEMPTS = 120; // 2 minutes max wait time
        const DELAY_MS = 1000; // 1 second pause between attempts
        const START_TIME = Date.now();

        console.log('Waiting for backend to be ready...');
        
        for (let i = 1; i <= MAX_ATTEMPTS; i++) {
            let timeoutId;
            try {
                // Create a timeout for fetch request
                const controller = new AbortController();
                timeoutId = setTimeout(() => controller.abort(), 5000);
                
                const res = await fetch(BACKEND_URL, {
                    signal: controller.signal
                });
                clearTimeout(timeoutId);
                
                // Accept 200 (OK) or 404 (not found but server is running)
                if (res.status === 200 || res.status === 404) {
                    const elapsed = Math.round((Date.now() - START_TIME) / 1000);
                    console.log(`Backend is ready after ${i} attempts (${elapsed}s)!`);
                    return;
                }
            } catch (error) {
                // Clear timeout if it was set
                if (timeoutId) {
                    clearTimeout(timeoutId);
                }
                
                // Backend not responding yet or timeout
                if (i % 10 === 0 || i <= 5) {
                    const elapsed = Math.round((Date.now() - START_TIME) / 1000);
                    console.log(`Waiting for backend to be ready... (attempt ${i}/${MAX_ATTEMPTS}, ${elapsed}s elapsed)`);
                }
            }

            // ⏸ Wait 1s before trying again
            await new Promise(r => setTimeout(r, DELAY_MS));
        }

        // If we get here, backend never started
        const totalTime = Math.round((Date.now() - START_TIME) / 1000);
        console.error(`Backend failed to start after ${MAX_ATTEMPTS} attempts (${totalTime} seconds)`);
        
        // Print container logs for debugging
        try {
            console.log('\n=== Application Container Logs ===');
            const appLogs = execSync('docker compose logs app --tail=100', {
                cwd: coreDir,
                encoding: 'utf-8'
            });
            console.log(appLogs);
        } catch (err) {
            console.error('Could not retrieve app logs:', err.message);
        }
        
        process.exit(1);

    } catch (err) {
        console.error('Failed to start Docker container', err);
        process.exit(1);
    }
};