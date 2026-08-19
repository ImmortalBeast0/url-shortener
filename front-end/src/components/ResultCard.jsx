import React, { useState } from 'react';
import { Copy, Check, ExternalLink, QrCode, Sparkles } from 'lucide-react';
import { QRCodeSVG } from 'qrcode.react';

export default function ResultCard({ result }) {
  const [copied, setCopied] = useState(false);
  const [showQr, setShowQr] = useState(false);

  if (!result) return null;

  const handleCopy = () => {
    navigator.clipboard.writeText(result.shortUrl);
    setCopied(true);
    setTimeout(() => setCopied(false), 2000);
  };

  return (
    <div className="glass-card p-6 rounded-2xl border border-teal-500/30 shadow-xl bg-gradient-to-br from-slate-900 via-slate-900/90 to-teal-950/30 animate-fade-in mt-6">
      <div className="flex items-center justify-between mb-3">
        <span className="inline-flex items-center gap-1.5 px-3 py-1 rounded-full text-xs font-semibold bg-teal-500/10 text-teal-300 border border-teal-500/20">
          <Sparkles className="w-3.5 h-3.5" /> Shortened Code: {result.shortCode}
        </span>
        <button
          onClick={() => setShowQr(!showQr)}
          className="text-xs font-medium text-slate-400 hover:text-teal-300 flex items-center gap-1 transition-colors px-2.5 py-1 rounded-lg bg-slate-800/80 border border-slate-700"
        >
          <QrCode className="w-3.5 h-3.5" /> {showQr ? 'Hide QR' : 'Show QR'}
        </button>
      </div>

      <div className="flex flex-col md:flex-row md:items-center justify-between gap-4 p-4 rounded-xl bg-slate-950/80 border border-slate-800">
        <div className="overflow-hidden">
          <p className="text-xs text-slate-400 font-medium mb-1">Your Short Link:</p>
          <a
            href={result.shortUrl}
            target="_blank"
            rel="noreferrer"
            className="text-lg md:text-xl font-bold text-teal-400 hover:text-teal-300 flex items-center gap-2 truncate transition-colors"
          >
            {result.shortUrl}
            <ExternalLink className="w-4 h-4 flex-shrink-0" />
          </a>
        </div>

        <button
          onClick={handleCopy}
          className={`py-3 px-6 rounded-xl font-bold flex items-center justify-center space-x-2 transition-all flex-shrink-0 ${
            copied
              ? 'bg-emerald-500 text-slate-950 shadow-lg shadow-emerald-500/20'
              : 'bg-slate-800 hover:bg-slate-700 text-white border border-slate-700'
          }`}
        >
          {copied ? (
            <>
              <Check className="w-4 h-4" />
              <span>Copied!</span>
            </>
          ) : (
            <>
              <Copy className="w-4 h-4" />
              <span>Copy Link</span>
            </>
          )}
        </button>
      </div>

      {showQr && (
        <div className="mt-4 p-4 rounded-xl bg-white text-slate-950 flex flex-col items-center justify-center animate-fade-in">
          <QRCodeSVG value={result.shortUrl} size={160} />
          <p className="text-xs font-bold text-slate-600 mt-2">Scan to test on mobile</p>
        </div>
      )}
    </div>
  );
}
