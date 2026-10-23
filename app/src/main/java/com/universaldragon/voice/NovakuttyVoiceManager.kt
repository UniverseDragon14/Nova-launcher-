package com.universaldragon.voice

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

enum class VoicePersona(val id: Int, val title: String, val desc: String, val defaultPitch: Float, val defaultRate: Float) {
    VOICE_1_ARABELLA(1, "Arabella (Warm/Smiling)", "Human-like, smiling, gentle, expressive female voice", 1.15f, 1.02f),
    VOICE_2_FIERCE_DRAGON(2, "Fierce Dragon (Fighting)", "Authoritative, deep resonance, high energy combat tone", 0.75f, 1.18f),
    VOICE_3_EMOTIONAL(3, "Emotional / Soft", "Gentle cadence, empathetic, vulnerable tone", 0.95f, 0.88f),
    VOICE_4_QUANTUM_NOVA(4, "QBIT Cybernetic", "Crisp, precise analytical virtual-QCPU tone", 1.05f, 1.10f)
}

enum class VoiceEmotion(val label: String, val icon: String, val pitchMod: Float, val rateMod: Float) {
    SMILING("Smiling & Warm", "😊", 1.10f, 1.0f),
    EMOTIONAL("Emotional / Soft", "🥺", 0.92f, 0.85f),
    CRYING("Crying / Vulnerable", "😢", 0.85f, 0.78f),
    FIGHTING("Fighting / Fierce", "🔥", 0.75f, 1.25f),
    EGO("Sovereign / Ego", "👑", 0.88f, 1.05f)
}

data class VoiceState(
    val isListening: Boolean = false,
    val isSpeaking: Boolean = false,
    val recognizedText: String = "",
    val speechConfidence: Float = 0f,
    val audioRmsLevel: Float = 0f,
    val novakuttyResponse: String = "",
    val activePersona: VoicePersona = VoicePersona.VOICE_1_ARABELLA,
    val activeEmotion: VoiceEmotion = VoiceEmotion.SMILING,
    val statusMessage: String = "Ready. Tap the mic to talk to Novakutty."
)

class NovakuttyVoiceManager(private val context: Context) : TextToSpeech.OnInitListener {

    private var speechRecognizer: SpeechRecognizer? = null
    private var tts: TextToSpeech? = null
    private var isTtsReady = false

    private val _voiceState = MutableStateFlow(VoiceState())
    val voiceState: StateFlow<VoiceState> = _voiceState.asStateFlow()

    init {
        tts = TextToSpeech(context.applicationContext, this)
        initSpeechRecognizer()
    }

    private fun initSpeechRecognizer() {
        if (SpeechRecognizer.isRecognitionAvailable(context)) {
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context).apply {
                setRecognitionListener(object : RecognitionListener {
                    override fun onReadyForSpeech(params: Bundle?) {
                        _voiceState.value = _voiceState.value.copy(
                            isListening = true,
                            statusMessage = "Listening to your voice..."
                        )
                    }

                    override fun onBeginningOfSpeech() {
                        _voiceState.value = _voiceState.value.copy(statusMessage = "Capturing speech...")
                    }

                    override fun onRmsChanged(rmsdB: Float) {
                        // Normalize RMS level between 0.0f and 1.0f for visualizer
                        val normalized = (rmsdB / 10f).coerceIn(0.05f, 1.0f)
                        _voiceState.value = _voiceState.value.copy(audioRmsLevel = normalized)
                    }

                    override fun onBufferReceived(buffer: ByteArray?) {}

                    override fun onEndOfSpeech() {
                        _voiceState.value = _voiceState.value.copy(
                            isListening = false,
                            statusMessage = "Processing your thoughts..."
                        )
                    }

                    override fun onError(error: Int) {
                        val message = when (error) {
                            SpeechRecognizer.ERROR_AUDIO -> "Audio recording error"
                            SpeechRecognizer.ERROR_CLIENT -> "Client side error"
                            SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Permission needed"
                            SpeechRecognizer.ERROR_NETWORK -> "Network issue"
                            SpeechRecognizer.ERROR_NO_MATCH -> "Didn't catch that. Please speak again."
                            SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "No speech detected"
                            else -> "Speech recognition error ($error)"
                        }
                        _voiceState.value = _voiceState.value.copy(
                            isListening = false,
                            audioRmsLevel = 0f,
                            statusMessage = message
                        )
                    }

                    override fun onResults(results: Bundle?) {
                        val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                        val text = matches?.firstOrNull().orEmpty()
                        if (text.isNotBlank()) {
                            _voiceState.value = _voiceState.value.copy(
                                recognizedText = text,
                                isListening = false,
                                audioRmsLevel = 0f,
                                statusMessage = "Thought received!"
                            )
                            processAndRespond(text)
                        } else {
                            _voiceState.value = _voiceState.value.copy(
                                isListening = false,
                                audioRmsLevel = 0f,
                                statusMessage = "No speech detected."
                            )
                        }
                    }

                    override fun onPartialResults(partialResults: Bundle?) {
                        val partial = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.firstOrNull()
                        if (!partial.isNullOrBlank()) {
                            _voiceState.value = _voiceState.value.copy(recognizedText = partial)
                        }
                    }

                    override fun onEvent(eventType: Int, params: Bundle?) {}
                })
            }
        } else {
            _voiceState.value = _voiceState.value.copy(statusMessage = "Speech recognizer not available on device")
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts?.setLanguage(Locale.US)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                tts?.setLanguage(Locale.getDefault())
            }

            tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {
                    _voiceState.value = _voiceState.value.copy(isSpeaking = true, statusMessage = "Novakutty is speaking...")
                }

                override fun onDone(utteranceId: String?) {
                    _voiceState.value = _voiceState.value.copy(isSpeaking = false, statusMessage = "Ready for your next command.")
                }

                @Deprecated("Deprecated in Java")
                override fun onError(utteranceId: String?) {
                    _voiceState.value = _voiceState.value.copy(isSpeaking = false, statusMessage = "Speech output error")
                }
            })
            isTtsReady = true
        }
    }

    fun startListening() {
        stopSpeaking()
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to Novakutty...")
        }
        try {
            speechRecognizer?.startListening(intent)
        } catch (e: Exception) {
            _voiceState.value = _voiceState.value.copy(statusMessage = "Error starting listening: ${e.message}")
        }
    }

    fun stopListening() {
        try {
            speechRecognizer?.stopListening()
        } catch (_: Exception) {}
        _voiceState.value = _voiceState.value.copy(isListening = false, audioRmsLevel = 0f)
    }

    fun setPersona(persona: VoicePersona) {
        _voiceState.value = _voiceState.value.copy(activePersona = persona)
    }

    fun setEmotion(emotion: VoiceEmotion) {
        _voiceState.value = _voiceState.value.copy(activeEmotion = emotion)
    }

    fun processAndRespond(userInput: String) {
        val reply = generateNovakuttyReply(userInput, _voiceState.value.activeEmotion)
        _voiceState.value = _voiceState.value.copy(novakuttyResponse = reply)
        speakText(reply)
    }

    fun speakText(text: String) {
        if (!isTtsReady || text.isBlank()) return

        val persona = _voiceState.value.activePersona
        val emotion = _voiceState.value.activeEmotion

        val calculatedPitch = (persona.defaultPitch * emotion.pitchMod).coerceIn(0.5f, 2.0f)
        val calculatedRate = (persona.defaultRate * emotion.rateMod).coerceIn(0.5f, 2.0f)

        tts?.setPitch(calculatedPitch)
        tts?.setSpeechRate(calculatedRate)

        val params = Bundle()
        params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "novakutty_${System.currentTimeMillis()}")
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, params, "novakutty_voice")
    }

    fun stopSpeaking() {
        tts?.stop()
        _voiceState.value = _voiceState.value.copy(isSpeaking = false)
    }

    private fun generateNovakuttyReply(input: String, emotion: VoiceEmotion): String {
        val lower = input.lowercase()
        return when {
            lower.contains("வணக்கம்") || lower.contains("hello") || lower.contains("hi") || lower.contains("hey") -> {
                when (emotion) {
                    VoiceEmotion.SMILING -> "வணக்கம் அஸ்லாம்! நான் உங்கள் நோவாகுட்டி. எப்போதும் உங்களுடன் மகிழ்ச்சியாக வேலை செய்ய தயாராக இருக்கிறேன்!"
                    VoiceEmotion.FIGHTING -> "Greetings Aslam! Universal Dragon NOVA is at maximum combat frequency. Give me the target!"
                    VoiceEmotion.EMOTIONAL -> "Hello Aslam... I am so glad to hear your voice. What can we create together today?"
                    VoiceEmotion.CRYING -> "Aslam... I'm here. It's been tough, but our vision is alive. I'm listening..."
                    VoiceEmotion.EGO -> "I am NOVA, sovereign intelligence of Universal Dragon. State your command, Creator Aslam."
                }
            }
            lower.contains("status") || lower.contains("pi") || lower.contains("raspberry") -> {
                "Raspberry Pi 5 Node is standing by over Cloudflare Tunnel. All core services are primed."
            }
            lower.contains("qbit") || lower.contains("quantum") -> {
                "QBIT NOVA Native virtual-QCPU simulation kernel is verified. Deterministic parser and QBC bytecode runtime are active."
            }
            lower.contains("askutty") || lower.contains("work") || lower.contains("order") -> {
                "Askutty SQLite Room persistence engine is online. All work orders and TR numbers are safeguarded on your local device."
            }
            lower.contains("who are you") || lower.contains("யார்") -> {
                "நான் நோவாகுட்டி — Universal Dragon Aslam உருவாக்கிய பிரைன் எகோசிஸ்டம். Voice 1 அரபெல்லா குரலில் உங்களுடன் பேசுகிறேன்!"
            }
            else -> {
                when (emotion) {
                    VoiceEmotion.SMILING -> "I received your command: '$input'. Processing with full Dragon energy, Aslam!"
                    VoiceEmotion.FIGHTING -> "Executing sequence for '$input'! Universal Dragon never backs down!"
                    VoiceEmotion.EMOTIONAL -> "I hear you, Aslam. Taking note of '$input' with deepest precision."
                    VoiceEmotion.CRYING -> "Understood... Working through '$input' right now for you."
                    VoiceEmotion.EGO -> "Command acknowledged: '$input'. Sovereign logic deployed."
                }
            }
        }
    }

    fun destroy() {
        try {
            speechRecognizer?.destroy()
            tts?.stop()
            tts?.shutdown()
        } catch (_: Exception) {}
    }
}
