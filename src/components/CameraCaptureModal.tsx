import React, { useRef, useState, useEffect } from 'react';
import { Camera, X, RefreshCw, Zap, ZapOff, ShieldAlert, Upload, CheckCircle2 } from 'lucide-react';
import { AppLanguage } from '../types';
import { t } from '../utils/translations';

interface CameraCaptureModalProps {
  language: AppLanguage;
  onCapture: (base64Image: string) => void;
  onClose: () => void;
  onUploadFallback?: () => void;
}

export const CameraCaptureModal: React.FC<CameraCaptureModalProps> = ({
  language,
  onCapture,
  onClose,
  onUploadFallback
}) => {
  const videoRef = useRef<HTMLVideoElement | null>(null);
  const canvasRef = useRef<HTMLCanvasElement | null>(null);
  const [stream, setStream] = useState<MediaStream | null>(null);
  const [facingMode, setFacingMode] = useState<'environment' | 'user'>('environment');
  const [permissionStatus, setPermissionStatus] = useState<'prompt' | 'granted' | 'denied'>('prompt');
  const [isCapturing, setIsCapturing] = useState(false);
  const [flashEffect, setFlashEffect] = useState(false);
  const [torchOn, setTorchOn] = useState(false);
  const [errorMessage, setErrorMessage] = useState<string | null>(null);

  const requestCamera = async (targetFacing = facingMode) => {
    try {
      setErrorMessage(null);
      if (stream) {
        stream.getTracks().forEach(track => track.stop());
      }

      // Check permissions API if available
      if (navigator.permissions && navigator.permissions.query) {
        try {
          const status = await navigator.permissions.query({ name: 'camera' as any });
          if (status.state === 'denied') {
            setPermissionStatus('denied');
          }
        } catch {
          // Ignore unsupported query
        }
      }

      const constraints: MediaStreamConstraints = {
        video: {
          facingMode: targetFacing,
          width: { ideal: 1280 },
          height: { ideal: 720 }
        },
        audio: false
      };

      const newStream = await navigator.mediaDevices.getUserMedia(constraints);
      setStream(newStream);
      setPermissionStatus('granted');

      if (videoRef.current) {
        videoRef.current.srcObject = newStream;
        videoRef.current.play().catch(() => {});
      }
    } catch (err: any) {
      if (err.name === 'NotAllowedError' || err.name === 'PermissionDeniedError') {
        setPermissionStatus('denied');
        setErrorMessage(t('cameraPermDesc', language.code));
      } else if (err.name === 'NotFoundError' || err.name === 'DevicesNotFoundError') {
        setPermissionStatus('denied');
        setErrorMessage('No camera device detected on this device. Please upload a photo from your gallery.');
      } else {
        setPermissionStatus('denied');
        setErrorMessage(err.message || 'Unable to access camera.');
      }
    }
  };

  useEffect(() => {
    requestCamera(facingMode);

    return () => {
      if (stream) {
        stream.getTracks().forEach(track => track.stop());
      }
    };
  }, [facingMode]);

  const toggleTorch = async () => {
    if (!stream) return;
    const videoTrack = stream.getVideoTracks()[0];
    if (videoTrack) {
      const capabilities = (videoTrack.getCapabilities?.() || {}) as any;
      if (capabilities.torch) {
        try {
          await videoTrack.applyConstraints({
            advanced: [{ torch: !torchOn } as any]
          });
          setTorchOn(!torchOn);
        } catch {
          // ignore
        }
      }
    }
  };

  const handleSnap = () => {
    if (!videoRef.current || !canvasRef.current) return;
    setIsCapturing(true);
    setFlashEffect(true);

    setTimeout(() => {
      setFlashEffect(false);
    }, 120);

    const video = videoRef.current;
    const canvas = canvasRef.current;
    canvas.width = video.videoWidth || 800;
    canvas.height = video.videoHeight || 600;

    const ctx = canvas.getContext('2d');
    if (ctx) {
      ctx.drawImage(video, 0, 0, canvas.width, canvas.height);
      const dataUrl = canvas.toDataURL('image/jpeg', 0.88);

      if (stream) {
        stream.getTracks().forEach(track => track.stop());
      }

      onCapture(dataUrl);
      onClose();
    }
  };

  const toggleFacingMode = () => {
    const nextMode = facingMode === 'environment' ? 'user' : 'environment';
    setFacingMode(nextMode);
    requestCamera(nextMode);
  };

  return (
    <div className="fixed inset-0 z-50 bg-black flex flex-col justify-between select-none">
      <canvas ref={canvasRef} className="hidden" />

      {/* Top Header Controls */}
      <div className="relative z-30 flex items-center justify-between px-5 py-4 bg-gradient-to-b from-black/85 via-black/40 to-transparent">
        <button
          onClick={onClose}
          className="p-2.5 rounded-full bg-black/60 text-white hover:bg-black/80 transition active:scale-95"
          aria-label="Close"
        >
          <X className="w-6 h-6" />
        </button>

        <div className="text-center">
          <span className="text-white text-sm font-bold tracking-wide drop-shadow flex items-center gap-1.5">
            <span className="w-2 h-2 rounded-full bg-emerald-400 animate-pulse"></span>
            {t('cameraModalTitle', language.code)}
          </span>
        </div>

        <div className="flex items-center gap-2">
          {permissionStatus === 'granted' && (
            <button
              onClick={toggleFacingMode}
              className="p-2.5 rounded-full bg-black/60 text-white hover:bg-black/80 transition active:scale-95"
              title="Flip Camera"
            >
              <RefreshCw className="w-5 h-5" />
            </button>
          )}
        </div>
      </div>

      {/* Viewfinder Center */}
      <div className="relative flex-1 flex items-center justify-center overflow-hidden bg-neutral-950">
        {permissionStatus === 'denied' ? (
          <div className="p-6 text-center max-w-sm bg-neutral-900/95 rounded-3xl border border-neutral-700/80 mx-5 shadow-2xl animate-in zoom-in-95 duration-200">
            <div className="w-14 h-14 rounded-2xl bg-amber-500/20 text-amber-400 flex items-center justify-center mx-auto mb-3.5 border border-amber-500/40">
              <ShieldAlert className="w-8 h-8" />
            </div>

            <h3 className="text-white font-extrabold text-base mb-1.5">
              {t('cameraPermTitle', language.code)}
            </h3>

            <p className="text-neutral-300 text-xs leading-relaxed mb-4">
              {errorMessage || t('cameraPermDesc', language.code)}
            </p>

            <div className="space-y-2.5">
              <button
                onClick={() => requestCamera(facingMode)}
                className="w-full py-3 px-4 bg-emerald-600 hover:bg-emerald-500 active:scale-98 text-white text-xs font-bold rounded-2xl shadow-lg transition flex items-center justify-center gap-2"
              >
                <Camera className="w-4 h-4" />
                {t('cameraPermAllowBtn', language.code)}
              </button>

              {onUploadFallback && (
                <button
                  onClick={() => {
                    onClose();
                    onUploadFallback();
                  }}
                  className="w-full py-2.5 px-4 bg-neutral-800 hover:bg-neutral-700 active:scale-98 text-neutral-200 text-xs font-semibold rounded-2xl border border-neutral-700 transition flex items-center justify-center gap-2"
                >
                  <Upload className="w-4 h-4 text-emerald-400" />
                  {t('cameraPermUploadFallback', language.code)}
                </button>
              )}
            </div>
          </div>
        ) : (
          <>
            <video
              ref={videoRef}
              playsInline
              muted
              autoPlay
              className="w-full h-full object-cover"
            />

            {/* Viewfinder Target Reticle */}
            <div className="absolute inset-0 pointer-events-none flex items-center justify-center p-6">
              <div className="w-72 h-72 sm:w-80 sm:h-80 rounded-3xl border-2 border-emerald-400/90 shadow-[0_0_0_9999px_rgba(0,0,0,0.5)] relative flex items-center justify-center">
                {/* Corner markers */}
                <div className="absolute top-2 left-2 w-5 h-5 border-t-3 border-l-3 border-emerald-400 rounded-tl-sm"></div>
                <div className="absolute top-2 right-2 w-5 h-5 border-t-3 border-r-3 border-emerald-400 rounded-tr-sm"></div>
                <div className="absolute bottom-2 left-2 w-5 h-5 border-b-3 border-l-3 border-emerald-400 rounded-bl-sm"></div>
                <div className="absolute bottom-2 right-2 w-5 h-5 border-b-3 border-r-3 border-emerald-400 rounded-br-sm"></div>

                {/* Prompt badge */}
                <div className="bg-black/70 backdrop-blur-md px-4 py-1.5 rounded-full text-white text-xs font-semibold border border-white/20 shadow-md">
                  {t('cameraAlignPrompt', language.code)}
                </div>
              </div>
            </div>

            {flashEffect && (
              <div className="absolute inset-0 bg-white z-40 transition-opacity duration-100" />
            )}
          </>
        )}
      </div>

      {/* Bottom Shutter & Controls */}
      <div className="relative z-30 pb-9 pt-5 px-6 bg-gradient-to-t from-black via-black/80 to-transparent flex items-center justify-around">
        {onUploadFallback && (
          <button
            onClick={() => {
              onClose();
              onUploadFallback();
            }}
            className="p-3 rounded-full bg-neutral-900/80 border border-neutral-700 text-neutral-300 hover:text-white transition"
            title="Upload from Gallery"
          >
            <Upload className="w-5 h-5" />
          </button>
        )}

        {/* Big Shutter button */}
        <button
          onClick={handleSnap}
          disabled={permissionStatus !== 'granted' || isCapturing}
          className={`p-1.5 rounded-full border-4 transition active:scale-95 ${
            permissionStatus === 'granted'
              ? 'border-white hover:border-emerald-300 cursor-pointer'
              : 'border-neutral-600 opacity-40 cursor-not-allowed'
          }`}
          aria-label="Capture Photo"
        >
          <div className="w-16 h-16 rounded-full bg-emerald-500 hover:bg-emerald-400 active:bg-emerald-600 flex items-center justify-center shadow-lg transition">
            <Camera className="w-7 h-7 text-white" />
          </div>
        </button>

        <button
          onClick={toggleFacingMode}
          disabled={permissionStatus !== 'granted'}
          className={`p-3 rounded-full bg-neutral-900/80 border border-neutral-700 text-neutral-300 hover:text-white transition ${
            permissionStatus !== 'granted' ? 'opacity-40' : ''
          }`}
          title="Switch Camera"
        >
          <RefreshCw className="w-5 h-5" />
        </button>
      </div>
    </div>
  );
};
