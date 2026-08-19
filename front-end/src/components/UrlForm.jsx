import React, { useState } from 'react';
import { ArrowRight, Sparkles, Link, Hash } from 'lucide-react';

export default function UrlForm({ onShorten, loading }) {
  const [url, setUrl] = useState('');
  const [customAlias, setCustomAlias] = useState('');

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!url.trim()) return;
    onShorten({ url: url.trim(), customAlias: customAlias.trim() });
  };

  return (
    <form onSubmit={handleSubmit} className="glass-card p-6 md:p-8 rounded-2xl shadow-2xl relative overflow-hidden border border-slate-800">
      <div className="absolute top-0 right-0 w-64 h-64 bg-teal-500/5 rounded-full blur-3xl pointer-events-none"></div>

      <div className="mb-6">
        <label className="block text-sm font-semibold text-slate-300 mb-2 flex items-center gap-2">
          <Link className="w-4 h-4 text-teal-400" /> Enter Long URL
        </label>
        <div className="relative">
          <input
            type="url"
            required
            placeholder="https://example.com/very/long/url/path/to/shorten"
            value={url}
            onChange={(e) => setUrl(e.target.value)}
            className="w-full px-4 py-3.5 pl-4 pr-12 rounded-xl bg-slate-900/90 border border-slate-700/80 text-white placeholder-slate-500 focus:outline-none focus:ring-2 focus:ring-teal-500/50 focus:border-teal-500 transition-all font-mono text-sm"
          />
        </div>
      </div>

      <div className="mb-6">
        <label className="block text-xs font-semibold text-slate-400 mb-2 flex items-center gap-1.5">
          <Hash className="w-3.5 h-3.5 text-indigo-400" /> Custom Alias (Optional)
        </label>
        <input
          type="text"
          placeholder="my-custom-link"
          value={customAlias}
          onChange={(e) => setCustomAlias(e.target.value)}
          className="w-full md:w-1/2 px-4 py-2.5 rounded-xl bg-slate-900/60 border border-slate-700/60 text-white placeholder-slate-500 focus:outline-none focus:ring-2 focus:ring-indigo-500/50 focus:border-indigo-500 transition-all text-sm"
        />
      </div>

      <button
        type="submit"
        disabled={loading || !url.trim()}
        className="w-full py-4 px-6 rounded-xl bg-gradient-to-r from-teal-500 to-emerald-500 hover:from-teal-400 hover:to-emerald-400 text-slate-950 font-bold flex items-center justify-center space-x-2 transition-all transform active:scale-[0.99] shadow-lg shadow-teal-500/25 disabled:opacity-50 disabled:cursor-not-allowed"
      >
        {loading ? (
          <div className="flex items-center space-x-2">
            <div className="w-5 h-5 border-2 border-slate-950 border-t-transparent rounded-full animate-spin"></div>
            <span>Hashing with Double Rolling Hash...</span>
          </div>
        ) : (
          <>
            <Sparkles className="w-5 h-5" />
            <span>Shorten URL</span>
            <ArrowRight className="w-5 h-5" />
          </>
        )}
      </button>
    </form>
  );
}
