import React, { useState, useEffect } from 'react';
import Header from './components/Header';
import UrlForm from './components/UrlForm';
import ResultCard from './components/ResultCard';
import RecentList from './components/RecentList';
import { AlertCircle, Zap, Database, ShieldCheck } from 'lucide-react';

export default function App() {
  const [currentResult, setCurrentResult] = useState(null);
  const [recentUrls, setRecentUrls] = useState([]);
  const [loading, setLoading] = useState(false);
  const [toast, setToast] = useState(null);

  const fetchRecentUrls = async () => {
    try {
      const res = await fetch('/api/v1/urls');
      if (res.ok) {
        const data = await res.json();
        setRecentUrls(data);
      }
    } catch (err) {
      console.warn('Backend server unreachable:', err);
    }
  };

  useEffect(() => {
    fetchRecentUrls();
  }, []);

  const handleShorten = async ({ url, customAlias }) => {
    setLoading(true);
    setToast(null);

    try {
      const res = await fetch('/api/v1/shorten', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ url, customAlias })
      });

      const data = await res.json();

      if (!res.ok) {
        throw new Error(data.error || data.message || 'Failed to shorten URL');
      }

      setCurrentResult(data);
      setRecentUrls((prev) => [data, ...prev.filter((u) => u.shortCode !== data.shortCode)]);
      setToast({ type: 'success', message: 'URL shortened using Double Rolling Hash!' });
    } catch (err) {
      setToast({ type: 'error', message: err.message });
    } finally {
      setLoading(false);
    }
  };

  const handleCopyToast = (shortUrl) => {
    navigator.clipboard.writeText(shortUrl);
    setToast({ type: 'info', message: 'Link copied to clipboard!' });
    setTimeout(() => setToast(null), 3000);
  };

  return (
    <div className="min-h-screen bg-slate-950 text-slate-100 flex flex-col justify-between relative glow-mesh selection:bg-teal-500 selection:text-slate-950">
      <Header />

      <main className="max-w-4xl mx-auto px-4 py-12 flex-1 w-full">
        {/* Hero section */}
        <div className="text-center mb-10">
          <div className="inline-flex items-center gap-2 px-3.5 py-1.5 rounded-full text-xs font-bold bg-teal-500/10 text-teal-300 border border-teal-500/20 mb-4">
            <Zap className="w-3.5 h-3.5" /> High-Performance DSA Double Rolling Hash
          </div>
          <h2 className="text-3xl md:text-5xl font-extrabold tracking-tight text-white mb-4">
            Shorten URLs with <span className="bg-gradient-to-r from-teal-400 via-emerald-300 to-indigo-400 bg-clip-text text-transparent">Precision Hashing</span>
          </h2>
          <p className="text-slate-400 text-sm md:text-base max-w-xl mx-auto">
            Powered by custom polynomial double rolling hash algorithm, PostgreSQL 18 persistent storage, and built-in IP rate limiter.
          </p>
        </div>

        {/* Toast Alert */}
        {toast && (
          <div
            className={`mb-6 p-4 rounded-xl flex items-center justify-between text-sm font-semibold animate-fade-in border ${
              toast.type === 'error'
                ? 'bg-rose-950/80 text-rose-300 border-rose-800'
                : 'bg-emerald-950/80 text-emerald-300 border-emerald-800'
            }`}
          >
            <div className="flex items-center gap-2">
              <AlertCircle className="w-5 h-5 flex-shrink-0" />
              <span>{toast.message}</span>
            </div>
            <button onClick={() => setToast(null)} className="text-xs opacity-70 hover:opacity-100">
              Dismiss
            </button>
          </div>
        )}

        {/* Form and Results */}
        <UrlForm onShorten={handleShorten} loading={loading} />
        <ResultCard result={currentResult} />
        <RecentList urls={recentUrls} onCopy={handleCopyToast} />
      </main>

      {/* Footer */}
      <footer className="border-t border-slate-900 bg-slate-950/80 py-6 text-center text-xs text-slate-500">
        <div className="max-w-4xl mx-auto px-4 flex flex-col md:flex-row items-center justify-between gap-3">
          <p>SnipURL © {new Date().getFullYear()} — Full Stack URL Shortener</p>
          <div className="flex items-center gap-4">
            <span className="flex items-center gap-1"><Database className="w-3.5 h-3.5 text-teal-400"/> PostgreSQL 18</span>
            <span className="flex items-center gap-1"><ShieldCheck className="w-3.5 h-3.5 text-indigo-400"/> Rate Limiter</span>
          </div>
        </div>
      </footer>
    </div>
  );
}
