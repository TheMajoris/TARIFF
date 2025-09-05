// tailwind.config.js
module.exports = {
	content: [
		"./src/**/*.{html,js,svelte,ts}", // scan all svelte files
	],
	theme: {
		extend: {},
	},
	plugins: [require("daisyui")], // enable DaisyUI
	daisyui: {
		themes: ["corporate"], // default theme = corporate
	},
};

