import React from 'react';
import { Link2, ShieldCheck, Cpu } from 'lucide-react';

export default function Header() {
  return (
    <header className="border-b border-slate-800/80 bg-slate-900/50 backdrop-blur-md sticky top-0 z-50">
      <div className="max-w-6xl mx-auto px-4 py-4 flex items-center justify-between">
        <div className="flex items-center space-x-3">
          <div className="w-10 h-10 rounded-xl bg-gradient-to-tr from-teal-500 to-indigo-500 flex items-center justify-center shadow-lg shadow-teal-500/20">
            <Link2 className="w-6 h-6 text-slate-950 font-bold" />
          </div>
          <div>
            <h1 className="text-xl font-bold bg-gradient-to-r from-teal-300 via-emerald-200 to-white bg-clip-text text-transparent">
              SnipURL
            </h1>
            <p className="text-xs text-slate-400 font-medium flex items-center gap-1">
              <Cpu className="w-3 h-3 text-teal-400" /> Double Rolling Hash Engine
            </p>
          </div>
        </div>

        <div className="flex items-center space-x-3">
          <span className="hidden sm:inline-flex items-center gap-1.5 px-3 py-1 rounded-full text-xs font-semibold bg-emerald-950/60 text-emerald-400 border border-emerald-500/30">
            <ShieldCheck className="w-3.5 h-3.5" /> Rate Limiter Active
          </span>
          <span className="px-3 py-1 rounded-full text-xs font-semibold bg-slate-800 text-slate-300 border border-slate-700">
            PostgreSQL 18
          </span>
        </div>
      </div>
    </header>
  );
}
