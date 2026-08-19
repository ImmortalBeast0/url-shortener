import React from 'react';
import { History, ExternalLink, MousePointerClick, Copy } from 'lucide-react';

export default function RecentList({ urls, onCopy }) {
  if (!urls || urls.length === 0) return null;

  return (
    <div className="glass-card p-6 rounded-2xl border border-slate-800 mt-8">
      <h3 className="text-lg font-bold text-white mb-4 flex items-center gap-2">
        <History className="w-5 h-5 text-teal-400" /> Recent Shortened Links
      </h3>

      <div className="space-y-3">
        {urls.map((item, idx) => (
          <div
            key={idx}
            className="p-4 rounded-xl bg-slate-900/60 border border-slate-800 flex flex-col md:flex-row md:items-center justify-between gap-3 hover:border-slate-700 transition-colors"
          >
            <div className="overflow-hidden space-y-1">
              <a
                href={item.shortUrl}
                target="_blank"
                rel="noreferrer"
                className="text-sm font-bold text-teal-400 hover:underline flex items-center gap-1.5 truncate"
              >
                {item.shortUrl}
                <ExternalLink className="w-3.5 h-3.5 flex-shrink-0" />
              </a>
              <p className="text-xs text-slate-400 truncate">{item.originalUrl}</p>
            </div>

            <div className="flex items-center justify-between md:justify-end gap-4 text-xs font-semibold text-slate-300">
              <span className="flex items-center gap-1 bg-slate-800 px-2.5 py-1 rounded-lg border border-slate-700">
                <MousePointerClick className="w-3.5 h-3.5 text-indigo-400" />
                {item.clickCount ?? 0} Clicks
              </span>

              <button
                onClick={() => onCopy(item.shortUrl)}
                className="p-2 rounded-lg bg-slate-800 hover:bg-slate-700 border border-slate-700 transition-colors"
                title="Copy short link"
              >
                <Copy className="w-4 h-4 text-slate-300" />
              </button>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}
