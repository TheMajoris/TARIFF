import adapter from '@sveltejs/adapter-static';
import { vitePreprocess } from '@sveltejs/vite-plugin-svelte';

/** @type {import('@sveltejs/kit').Config} */
const config = {
    // Consult [https://svelte.dev/docs/kit/integrations](https://svelte.dev/docs/kit/integrations)
    // for more information about preprocessors
    preprocess: vitePreprocess(),

    kit: {
        // Use the static adapter to output a folder of static files
        adapter: adapter({
            // This is the directory where the build output will be placed.
            // It must match the directory your Dockerfile copies from.
            pages: 'build',
            assets: 'build',
            fallback: 'index.html',
            precompress: false
        })
    }
};

export default config;
