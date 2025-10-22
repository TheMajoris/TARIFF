// Fetch news articles from the backend API
export async function fetchNews() {
  try {
    const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api/v1';
    console.log('Fetching news from:', `${API_BASE_URL}/news`);
    
    const res = await fetch(`${API_BASE_URL}/news`);
    
    if (!res.ok) {
      throw new Error(`Failed to fetch news: ${res.status} ${res.statusText}`);
    }
    
    const response = await res.json();
    console.log('Raw news data:', response);
    
    // Map the response to match frontend expectations
    const mappedNews = response.articles.map((article) => ({
      id: article.id,
      title: article.headline,
      summary: article.summary,
      link: article.url,
      tags: article.tags || [],
      date: article.publishedDate || "Date unavailable" // Use actual date or indicate missing
    }));
    
    // Sort by most recent first (since we don't have actual dates, sort by ID descending)
    mappedNews.sort((a, b) => (b.id || 0) - (a.id || 0));
    
    console.log('Mapped news:', mappedNews);
    return mappedNews;
  } catch (err) {
    console.error("fetchNews error:", err);
    return []; // Return empty array if error occurs
  }
}

// Fetch news by specific tags
export async function fetchNewsByTags(tags) {
  try {
    const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api/v1';
    console.log('Fetching news by tags:', tags);
    
    const params = new URLSearchParams();
    tags.forEach((tag) => params.append('tags', tag));
    
    const res = await fetch(`${API_BASE_URL}/news?${params.toString()}`);
    
    if (!res.ok) {
      throw new Error(`Failed to fetch news by tags: ${res.status} ${res.statusText}`);
    }
    
    const response = await res.json();
    console.log('Raw news by tags data:', response);
    
    // Map the response to match frontend expectations
    const mappedNews = response.articles.map((article) => ({
      id: article.id,
      title: article.headline,
      summary: article.summary,
      link: article.url,
      tags: article.tags || [],
      date: null // No fallback date for undated articles
    }));
    
    // Sort by most recent first (since we don't have actual dates, sort by ID descending)
    mappedNews.sort((a, b) => (b.id || 0) - (a.id || 0));
    
    console.log('Mapped news by tags:', mappedNews);
    return mappedNews;
  } catch (err) {
    console.error("fetchNewsByTags error:", err);
    return []; // Return empty array if error occurs
  }
}
