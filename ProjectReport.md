# Introduction #

---

This project is interested in investigating the practicality and reliability of acoustic modems as a means of wireless mobile-to-mobile communication. Sound has a long history in digital communications, in particular in underwater communication systems. However, it has largely, yet understandably, been dismissed in aerial communications. Most digital communication systems are designed to maximize data transmission rates, minimize the required bandwidth and power consumption, and provide a low probability of error. The data rates for sound are much lower than for typical radio communication media (e.g., Bluetooth, WiFi, cellular, ZigBee, RFID). Sound can also be annoying, unpleasant, and distracting. On the other hand, sound possesses some unique characteristics that may make it desirable for some mobile applications. It is inherently localized and cannot penetrate walls or propagate over long distances in the air like higher frequency communication media. It does not require a line-of-sight as with optical media (e.g., infrared). Furthermore, an audio communication system can be deployed relatively easily using commodity mobile device hardware (a speaker and microphone) with the coding and decoding performed entirely in software.

The increasing density of mobile devices enables many envisioned localized peer-to-peer applications. As commercial off-the-shelf (COTS) smartphones become more powerful, it is worthwhile to revisit the use of sound as a medium for aerial digital device-to-device communications. This project extends existing work on acoustic modems and investigates practical issues of deploying an acoustic device-to-device communication system in mobile settings.

```
Contents
```


# Sound in Digital Communications #

---

Water is a poor medium for radio and optical signal propagation, but excellent for sound propagation [(1)](ProjectReport#References.md). Since the 1940s, sound has been used by submarine crews to position, detect objects and enemy craft, and to communicate. Advances in digital signal processing (DSP) hardware and techniques have helped make underwater acoustic modems possible resulting in an increased interest in acoustics for underwater communications [(2)](ProjectReport#References.md) and underwater wireless sensor networks [(3)](ProjectReport#References.md). Several underwater acoustic modems are available as commercial products [(4,5,6)](ProjectReport#References.md) and research platforms [(7,8,9)](ProjectReport#References.md). Typically, these modems make use of upper audible and lower ultrasonic bands (12-30 KHz).

|![http://mobile-acoustic-modems-in-action.googlecode.com/files/fig_uwsn.jpg](http://mobile-acoustic-modems-in-action.googlecode.com/files/fig_uwsn.jpg)|
|:------------------------------------------------------------------------------------------------------------------------------------------------------|
|**Figure 1.** Underwater wireless sensor network.                                                                                                      |

Sound has also been employed for long-distance point-to-point communications in telephone line modems. Acoustically coupled modems invented in the 1960s used a voice band (1-3 KHz) to modulate data and required a user to place a telephone receiver onto the modem. These remained popular through the 1980s until the invention of the direct-connect softmodems in the 1990s which directly connected the modem hardware with a telephone line.

The Zenith Space Command remote control, invented in the 1950s, was the first example of device-to-device communication using an acoustic (ultrasonic) channel. Ultrasonic acoustic signaling mechanisms have since been used to help support localization (e.g., in [(10)](ProjectReport#References.md)), but in general ultrasonic applications require specialized hardware.

Digital aerial acoustic communications were first investigated in the mid-1990s within the context of steganography [(11)](ProjectReport#References.md), wherein digital information is hidden within an analog signal (e.g., a digital watermark hidden in a recording of a song). In the early 2000s, two notable research projects explored the use of non-lexical (non-spoken) sound for computer-to-computer interaction. The Things that Talk project [(12)](ProjectReport#References.md) evaluated a number of traditional modulation techniques (e.g., ASK, FSK) according to metrics including data rate, computational overhead, noise tolerance, and disruption level. Taking a cue from Things that Talk, the Digital Voices project [(13,14)](ProjectReport#References.md) explored these techniques further with an emphasis on human perception of acoustic device-to-device communication. Motivated by the observation that traditional modulation techniques are not pleasant to the human ear when played through the air, Digital Voices studied how data-carrying audio signals could be made to mimic music and other pleasant sounds by playing around with some common modulation parameters. These authors developed several musically oriented ([melodies](http://www.ics.uci.edu/~lopes/dv/Bach2.wav), [instruments](http://www.ics.uci.edu/~lopes/dv/pbc4bps.wav), [tones](http://www.ics.uci.edu/~lopes/dv/askpent01.wav)), biologically inspired ([crickets](http://www.ics.uci.edu/~lopes/dv/synt_cricket.wav), birds), and just-for-fun ([R2-D2](http://www.ics.uci.edu/~lopes/dv/this.wav)) variations of traditional modulation techniques.

In the last ten years, smartphones have gained extreme popularity and have become very powerful sensing, computational, and communication platforms. Recently, several research projects have investigated the use of acoustics within mobile applications. The authors of [(15)](ProjectReport#References.md) and [(16)](ProjectReport#References.md) use COTS mobile phones and a sample counting technique to perform high accuracy device ranging and gesture-based device pairing, respectively. Escort [(17)](ProjectReport#References.md) is a localization system that employs the same sample counting technique and acoustic beacons to recursively infer a mobile user’s location from a series of device-to-device contacts. The authors of [(18)](ProjectReport#References.md) measure the induced Doppler effect between acoustic waves between two mobile devices to estimate the direction from one device to the other.

This project extends one of the Digital Voices musically oriented modulation techniques to investigate the practical use of acoustic signals for ad hoc device-to-device data transmission between COTS mobile devices.

# The Case for an Audible Bandwidth #

---

While an acoustic channel cannot achieve the high bit rates and low latency of higher frequency media (e.g., RF, infrared), sound possesses some unique features that may be particularly desirable for mobile applications requiring device-to-device communications [(12)](ProjectReport#References.md). An ultrasonic acoustic bandwidth is not considered here because COTS mobile device speakers and microphones are designed solely to capture and play back audible frequencies.
  * Sending and receiving data on an acoustic channel only requires commodity hardware, a speaker and a microphone, which are available on any smartphone. Data coding and decoding may be implemented entirely in software.
  * Sound is inherently localized. Unlike RF communication technologies (e.g., Bluetooth, WiFi, cellular) that penetrate walls and can propagate over many feet and even miles, sound is easily contained within rooms and only travels several feet. This characteristic may be desirable for applications that require a stricter notion of localization.
  * Unlike optical media (e.g., infrared), sound does not require a direct line-of-sight.
  * Using sound as a communication medium exposes communication to humans. This feature may be a hindrance. However, some applications may actually benefit from human awareness of device-to-device communication.

|![http://mobile-acoustic-modems-in-action.googlecode.com/files/fig_sound_locality.png](http://mobile-acoustic-modems-in-action.googlecode.com/files/fig_sound_locality.png)|
|:--------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
|**Figure 2.** Sound is inherently localized.                                                                                                                               |

# Application Examples #

---

Mobile applications desiring localized ad hoc communication that doesn’t require high bit rates can use sound. A good example of such an application is mobile service discovery. Mobile devices may use an acoustic modem to periodically broadcast some small piece of network or service identification information (e.g., an IP address or an SSID). Nearby devices that hear and decode this information may establish a connection to the broadcasting device over a higher bit rate RF (e.g., Internet) connection to perform subsequent application-dependent tasks.

|![http://mobile-acoustic-modems-in-action.googlecode.com/files/fig_acomm_discovery.png](http://mobile-acoustic-modems-in-action.googlecode.com/files/fig_acomm_discovery.png)|
|:----------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
|**Figure 3.** Using an acoustic modem to discovery nearby mobile devices.                                                                                                    |

The relatively poor propagation of sound in air and through walls can be advantageous for mobile applications that require secure device-to-device data sharing. For example, if a group of colleagues conducting a meeting in a public place (e.g., a coffee shop or café), or even in a meeting room, wish to establish a secure ad hoc network using a “leaky” RF channel they may first acquire a secure cryptographic keypair from the meeting leader using an acoustic channel to henceforth send and receive sensitive data over the RF channel.

Exchanging business cards or personal URLs is common at networking events in many business and academic fields. Colleagues meeting one another could exchange personalized audio glyphs containing their contact information or the URL of their personal research homepage. This could be an effective and fun way of striking up conversations with new acquaintances.

# Practical Challenges for Mobile Acoustic Modems #

---

Designing a general-purpose acoustic modem for mobile applications is difficult. First, the channel is imperfect and second, the mobile device audio hardware possesses inconsistencies, faults, and processing limitations.

An acoustic channel is far from ideal for digital communication purposes. The Doppler effect is particularly noticeable. Even the slightest amount of movement can induce relatively large perceived frequency differences between transmitters and receivers. For example, the velocity difference that would result from two people walking in opposite directions (about 3 m/s) would result in a perceived frequency difference of about 1%. Everyday environments are noisy too. The figures below (**Figure 4**) show some frequency analysis of ambient noise around The University of Texas at Austin campus. These environments are particularly noisy in a mobile acoustic modem’s operating band (the 1-7 KHz band).

|![http://mobile-acoustic-modems-in-action.googlecode.com/files/fig_ambient_noise.png](http://mobile-acoustic-modems-in-action.googlecode.com/files/fig_ambient_noise.png)|
|:------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
|**Figure 4.** Ambient noise around the UT Austin campus.                                                                                                                 |

Challenges exist on the device side as well. Hardware is inconsistent between devices from different manufacturers and even between devices from the same manufacturer. A tone that sounds loud and clear played on one smartphone may be quiet or scratchy on another. This results from differences and nonuniformities in devices’ speaker and microphone frequency responses and characteristics. Smartphones are becoming exceptionally powerful. However, the processing requirements of an acoustic modem’s DSP components that encode and decode continuous streams of audio signals in software can noticeably impact mobile devices’ battery life and performance.

# Mobile Acoustic Modem Implementations #

---

This project investigates the practicality and reliability of mobile acoustic modems using two of the acoustically pleasant modulation variations developed as part of Digital Voices, both implemented on the Android platform. These modulation techniques and their parameters are described in detail in [(13)](ProjectReport#References.md), but some general implementation details are provided here.

## Musically Oriented Data Modulation ##

The primary modulation technique used in this project’s experiments is a musically oriented variation of a binary amplitude-shift keyring (B-ASK). Given a string of ASCII characters, each character’s bits are encoded to one of eight tones (one for each bit of each character (byte)) separated by pentatonic steps.

|![http://mobile-acoustic-modems-in-action.googlecode.com/files/fig_pentatonic_txr.png](http://mobile-acoustic-modems-in-action.googlecode.com/files/fig_pentatonic_txr.png)|
|:--------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
|**Figure 5.** Encoding `N` bits using the pentatonic codec.                                                                                                                |

The presence or absence of a tone at one of these given frequencies indicates a binary one or zero, respectively. The encoding of the letter “a” is illustrated in **Figure 6**.

|![http://mobile-acoustic-modems-in-action.googlecode.com/files/fig_pentatonic_ex.png](http://mobile-acoustic-modems-in-action.googlecode.com/files/fig_pentatonic_ex.png)|
|:------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
|**Figure 6.** Encoding the character "a" using the pentatonic codec.                                                                                                     |

A data transmission (**Figure 7**) is preceded by a Hail tone, which enables a receiver to synchronize to the transmission in time, and a set of Calibration signals, which allows a receiver to equalize out nonuniformities in the received audio signal.

|![http://mobile-acoustic-modems-in-action.googlecode.com/files/fig_pentatonic_tx.png](http://mobile-acoustic-modems-in-action.googlecode.com/files/fig_pentatonic_tx.png)|
|:------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
|**Figure 7.** A data transmission produced by the pentatonic codec.                                                                                                      |

A receiver continuously performs a simple correlated frequency detection function (**Figure 8**) to detect the presence or absence of certain frequencies.

|![http://mobile-acoustic-modems-in-action.googlecode.com/files/fig_corr_rx.png](http://mobile-acoustic-modems-in-action.googlecode.com/files/fig_corr_rx.png)|
|:------------------------------------------------------------------------------------------------------------------------------------------------------------|
|**Figure 8.** Correlated receiver.                                                                                                                           |

The code developed for the mobile-to-mobile experiments here is based on the Android implementation of this pentatonic coder-decoder (codec), which is publicly available on the Digital Voices GitHub repository [(19)](ProjectReport#References.md).

|<a href='http://www.youtube.com/watch?feature=player_embedded&v=aZG3wDxsiXw' target='_blank'><img src='http://img.youtube.com/vi/aZG3wDxsiXw/0.jpg' width='425' height=344 /></a>|
|:--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
|**Video 1.** Pentatonic codec in action.                                                                                                                                         |

## Biologically Inspired Data Modulation ##

The second Digital Voices modulation technique investigated in this project employs a combination of ASK, phase shift, and pulse width modulation (PWM). The encoding process begins by feeding a string of ASCII characters through a Huffman encoder producing a sequence of the numbers one through four. The Digital Voices authors observed that crickets chirp in bursts of three quick chirps. To mimic this behavior, a data transmission is broken into equally sized time slots, or frames. Next, every four digits of the Huffman-compressed sequence are encoded to a three-chirp “packet.” The last three digits of every block of four are encoded to one of four amplitude-pulse width pairs.

|![http://mobile-acoustic-modems-in-action.googlecode.com/files/fig_cricket_txr.png](http://mobile-acoustic-modems-in-action.googlecode.com/files/fig_cricket_txr.png)|
|:--------------------------------------------------------------------------------------------------------------------------------------------------------------------|
|**Figure 9.** Encoding `N` integers using the cricket codec.                                                                                                         |

The first digit of every block of four indicates the phase of the frame – where the three chirps occur within the frame. The encoding of the string “aeiou” is shown in **Figure 10**.

|![http://mobile-acoustic-modems-in-action.googlecode.com/files/fig_cricket_ex.png](http://mobile-acoustic-modems-in-action.googlecode.com/files/fig_cricket_ex.png)|
|:------------------------------------------------------------------------------------------------------------------------------------------------------------------|
|**Figure 10.** Encoding the string "aeiou" using the cricket codec.                                                                                                |

Similar to the pentatonic codec, a data transmission encoded with the cricket codec (**Figure 11**) is preceded by a Hail signal to enable a receiver to synchronize to the transmission and equalize out any nonuniformities in the received audio signal. A Hail signal is also appended to each transmission to indicate an end of transmission. A received audio signal is decoded using the same correlation function as the pentatonic codec. The width of pulse is determined by taking its Euclidian norm.

|![http://mobile-acoustic-modems-in-action.googlecode.com/files/fig_cricket_tx.png](http://mobile-acoustic-modems-in-action.googlecode.com/files/fig_cricket_tx.png)|
|:------------------------------------------------------------------------------------------------------------------------------------------------------------------|
|**Figure 11.** A data transmission produced by the cricket codec.                                                                                                  |

As part of this project, the cricket codec was implemented on the Android platform. This code has been added to the Digital Voices GitHub repository [(19)](ProjectReport#References.md).

|<a href='http://www.youtube.com/watch?feature=player_embedded&v=KZu_MOloq2M' target='_blank'><img src='http://img.youtube.com/vi/KZu_MOloq2M/0.jpg' width='425' height=344 /></a>|
|:--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
|**Video 2.** Cricket codec in action.                                                                                                                                            |

## General Receiver Architecture ##

**Figure 12** illustrates the general architecture of a receiver in the Android implementations. A `MicrophoneListener` thread continuously records two-second blocks of 16-bit audio. Next, a block of 16-bit audio is converted to 8-bit audio. A `StreamDecoder` thread performs packet detection and symbol-to-bit conversion, in the case of the pentatonic codec, or symbol-to-integer conversion, in the case of the cricket codec, using the correlation function above. Finally, the bits or integers are decoded.

|![http://mobile-acoustic-modems-in-action.googlecode.com/files/fig_rx_arch.png](http://mobile-acoustic-modems-in-action.googlecode.com/files/fig_rx_arch.png)|
|:------------------------------------------------------------------------------------------------------------------------------------------------------------|
|**Figure 12.** Receiver architecture.                                                                                                                        |

# Application Layer Techniques for Reliability #

---

The Digital Voices project demonstrated the feasibility of acoustic modems for device-to-device communications in ordinary environments. The modulation variations developed in that project were designed to be robust to common ambient noise (e.g., talking). However, there was little quantitative evaluation of these techniques’ reliability and robustness. As mobile devices and smartphones become increasingly powerful, acoustic modems may present themselves as desirable inter-device communication mechanisms for some applications. Developers wishing to design or implement an acoustic modem for a mobile application may want to have quantitative notion of the physical parameters this medium adheres to.

This project is interested in investigating the practicality and reliability of simple acoustic modems for mobile-to-mobile communications by applying a traditional wireless communication evaluation. This section presents and evaluates two application layer techniques for providing reliable communication over a lossy acoustic channel.

## Robustness through Redundancy ##

Forward Error Correction (FEC) is a common way of mitigating data loss in a noisy or lossy channel. An **_(n,k)_** FEC code encodes **_k_** blocks of source data to `n` blocks of encoded data such that any **_k_** the original source data can be reconstructed from any subset of **_k_** encoded blocks. This is illustrated in **Figure 13**.

|![http://mobile-acoustic-modems-in-action.googlecode.com/files/fig_fec.png](http://mobile-acoustic-modems-in-action.googlecode.com/files/fig_fec.png)|
|:----------------------------------------------------------------------------------------------------------------------------------------------------|
|**Figure 13.** Forward error correction.                                                                                                             |

To investigate the utility of FEC for acoustic modems I implemented a Reed Solomon FEC scheme as a drop-in component on the Android platform. The Reed Solomon encoder produces **_n-k_** parity bytes, which can detect and correct up to _**n-k**_ erasures (corrupted bytes) by the decoder. Parity bytes are appended to a transmission.

|![http://mobile-acoustic-modems-in-action.googlecode.com/files/fig_pentatonic_tx-fec.png](http://mobile-acoustic-modems-in-action.googlecode.com/files/fig_pentatonic_tx-fec.png)|
|:--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
|**Figure 14.** A data transmission with appended FEC parity bytes.                                                                                                               |

Using the Digital Voices pentatonic codec I investigated the impact of different amounts of redundancy on the robustness of acoustic data transfer. The experimental setup is depicted in **Figure 15**. I placed two LG Optimus 3D P920 smartphones side-by-side. These two devices acted as my transmitter (Tx) and receiver (Rx). A third device (an HTC myTouch smartphone) was placed a variable distance away and was programmed to play steady noise at the eight frequencies of the pentatonic codec. At a given distance the signal to noise ratio (SNR) was measured at the Rx device by playing a short calibration sequence from the Tx device (the signal) then from the interfering device (the noise). Next, while the noise signal was played from the interfering device, the Tx device would transmit a predetermined 16-byte data sequence. The bit error rate (BER) was then calculated at the Rx device. This procedure was performed with the interfering device at 11 different distances from the Rx device (from 50 inches down to 2 inches). At each separation the SNR and BER measurement was repeated five times (a total of 55 measurements per experiment). These experiment were conducted using no FEC, 25%, 50%, 75%, and 100% FEC – i.e., 0, 4, 8, 12, and 16 parity bytes.

|![http://mobile-acoustic-modems-in-action.googlecode.com/files/fig_exp_setup.png](http://mobile-acoustic-modems-in-action.googlecode.com/files/fig_exp_setup.png)|
|:----------------------------------------------------------------------------------------------------------------------------------------------------------------|
|**Figure 15.** Experimental setup.                                                                                                                               |

**Figure 16** shows a log-log plot of the BER vs. SNR. In general, each of these experiments follows the same trend. Looking closer at each end of the plot reveals some interesting behavior. **Figure 17** is a close up of the same plot at the low SNR end (0-10 dB). As expected, the addition of more FEC parity bytes results in lower BER. We would expect the same behavior at the opposite end of the plot as well; however, this was not observed. **Figure 18** shows the upper SNR end (15-25 dB). These results show that the addition of FEC parity bytes actually produces higher BER. This effect is a product of the Reed Solomon implementation. In my implementation FEC decoding is always performed and requires all `n-k` parity bytes. Errors in parity bytes can actually induce errors in correct data. Therefore, at higher SNR levels more redundancy actually results in more susceptibility to improper decoding. A better tactic would be to employ a cyclic redundancy check (CRC) to check if FEC decoding was even necessary upon reception of a transmission.

|![http://mobile-acoustic-modems-in-action.googlecode.com/files/fig_ber_vs_snr.png](http://mobile-acoustic-modems-in-action.googlecode.com/files/fig_ber_vs_snr.png)|
|:------------------------------------------------------------------------------------------------------------------------------------------------------------------|
|**Figure 16.** BER vs. SNR for different amounts of FEC parity bytes.                                                                                              |

|![http://mobile-acoustic-modems-in-action.googlecode.com/files/fig_ber_vs_snr_lowhalf.png](http://mobile-acoustic-modems-in-action.googlecode.com/files/fig_ber_vs_snr_lowhalf.png)|
|:----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
|**Figure 17.** BER vs. SNR, low SNR end.                                                                                                                                           |

|![http://mobile-acoustic-modems-in-action.googlecode.com/files/fig_ber_vs_snr_highhalf.png](http://mobile-acoustic-modems-in-action.googlecode.com/files/fig_ber_vs_snr_highhalf.png)|
|:------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
|**Figure 18.** BER vs. SNR, high SNR end.                                                                                                                                            |

Another common practice in FEC is to perform multiple layers of encoding, deterministically shuffling the encoded blocks between each layer of encoding. This scheme is particularly useful for promoting uniformity of loss at the receiver in channels prone to bursty losses. Bursts of erasures in encoded data will be uniformly distributed across the decoded data.

Following the same encoding and inter-layer block shuffling approach used to encode Compact Discs, I ran a single BER vs. SNR experiment using two layers of 4 parity byte FEC to encode the same 16-byte data sequence as in the previous experiments. In between the two layers of encoding the data and parity bytes were four-way interleaved.

**Figure 19** shows the same plot as before with the addition of this two layer encoding experiment. Interestingly, the two layer encoding performs even worse than using no FEC at low SNR levels, but above an SNR of 15 dB outperforms all five of the previous schemes. At low SNR levels it’s likely that the additional FEC layer amplified decoding errors. However, at higher SNR levels the layer technique acts as a guard against minor losses making transmissions less susceptible to parity errors.

|![http://mobile-acoustic-modems-in-action.googlecode.com/files/fig_ber_vs_snr_interleaving.png](http://mobile-acoustic-modems-in-action.googlecode.com/files/fig_ber_vs_snr_interleaving.png)|
|:--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
|**Figure 19.** BER vs. SNR with two layer FEC and four way interleaving.                                                                                                                     |

## Robustness through Collaboration ##

Sometimes the acoustic channel may be persistently noisy (e.g., in a crowded environment). In these situations an FEC scheme can be beneficial. In less noisy environments instantaneously induced errors, due to quick movements or brief loud noises for example, FEC is not the right tool. In these cases a simple retry may suffice. In this vein, I was interested in how collaboration could be used to enable a group of devices to autonomously agree upon a piece of information.

Consider a scenario similar to that illustrated in **Figure 20** where a device wishes to broadcast a piece of information (e.g., an SSID, IP address, or cryptographic keypair) to a group of close by devices. Perhaps one of the devices is too far away to correctly decode the broadcast and must signal for help from a closer device that did correctly decode the broadcast.

|![http://mobile-acoustic-modems-in-action.googlecode.com/files/fig_collaborative.png](http://mobile-acoustic-modems-in-action.googlecode.com/files/fig_collaborative.png)|
|:------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
|**Figure 20.** Collaborative group agreement.                                                                                                                            |

To enable this type of collaborative interaction I mocked up a collaborative sharing application, _Stop, Collaborate & Listen_, which is available in this project’s source code repository and on the [Downloads](http://code.google.com/p/mobile-acoustic-modems-in-action/downloads/list) page. This application again employs the Digital Voices pentatonic codec and a single byte CRC (CRC-8-CCITT).

The key motivating idea of the application was to ensure that a device signaling for help would receive an assisting rebroadcast from the closest device with the correct broadcast. To accomplish this we can employ a sample counting technique. Given that each of the participating devices uses the same sampling rate and waits the same amount of time before responding to a signal for help, a device **_D1_** can quite simply infer that it is closest to another device **_D2_** that has signaled for help if the help signal is in **_D1_**’s buffer and no assisting response has been heard and the agreed upon helping period is up. This behavior is built into the collaborative application’s state machine (**Figure 21**). However, in practice it was difficult to achieve given the fine balance and inconsistent switching delay between the application’s listening and playing threads.

|![http://mobile-acoustic-modems-in-action.googlecode.com/files/fig_state_machine.png](http://mobile-acoustic-modems-in-action.googlecode.com/files/fig_state_machine.png)|
|:------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
|**Figure 21.** _Stop, Collaborate & Listen_ state machine, bolded states are displayed in the UI.                                                                        |

Below are some demonstration videos of _Stop, Collaborate & Listen_ in action in different scenarios.

|<a href='http://www.youtube.com/watch?feature=player_embedded&v=34Vjg7P3qZo' target='_blank'><img src='http://img.youtube.com/vi/34Vjg7P3qZo/0.jpg' width='425' height=344 /></a>|
|:--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
|**Video 3.** Two devices with a bad broadcast and a good _S.O.S._ recovery.                                                                                                      |

|<a href='http://www.youtube.com/watch?feature=player_embedded&v=4PuEoTzZkWw' target='_blank'><img src='http://img.youtube.com/vi/4PuEoTzZkWw/0.jpg' width='425' height=344 /></a>|
|:--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
|**Video 4.** Four devices with a good broadcast.                                                                                                                                 |

|<a href='http://www.youtube.com/watch?feature=player_embedded&v=POnBVHKhXOw' target='_blank'><img src='http://img.youtube.com/vi/POnBVHKhXOw/0.jpg' width='425' height=344 /></a>|
|:--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
|**Video 5.** Four devices with a bad broadcast and a clean _S.O.S._ recovery.                                                                                                    |

|<a href='http://www.youtube.com/watch?feature=player_embedded&v=CE61qtPLa_8' target='_blank'><img src='http://img.youtube.com/vi/CE61qtPLa_8/0.jpg' width='425' height=344 /></a>|
|:--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
|**Video 6.** Four devices deadlocked in _S.O.S._ recovery (uh oh).                                                                                                               |

|<a href='http://www.youtube.com/watch?feature=player_embedded&v=A8uJ9-fiDiQ' target='_blank'><img src='http://img.youtube.com/vi/A8uJ9-fiDiQ/0.jpg' width='425' height=344 /></a>|
|:--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
|**Video 7.** Three devices with one out of range, the other two help out.                                                                                                        |

# Conclusions & Future Work #

---

This project explored the use, practicality, and reliability of acoustic modems for mobile inter-device digital communications. There’s no doubt that mobile acoustic modems are feasible, but practical challenges exist when designing them for highly mobile scenarios. The experiments conducted in this project demonstrate that even some fairly simple application layer techniques can be employed to mitigate factors like low SNR or a sporadically lossy channel. However, persistent decoding errors due to the effects of Doppler, for example, must be addressed at the physical layer. This project did not explore or implement any techniques to mitigate Doppler effects, but doing so would be an excellent starting point for future work.

A key future challenge for mobile acoustic modems is maintaining simplicity and designing for efficiency. Underwater acoustic modems make use of specialized DSP hardware to perform complex processing very quickly. Mobile acoustic modems, on the other hand, must be implemented entirely in software. Subjecting a mobile device to continuous heavy processing will drain the battery very quickly. I experienced this myself during my experiments – the pentatonic codec could drain a receiver device’s battery completely in about 20 minutes. This highlights the tradeoff between robustness and practicality. Future work must keep this tradeoff in mind and design codecs with concerns like power usage accounted for.

# References #

---

  1. J. Partan, J. Kurose, and B. N. Levine, “A survey of practical issues in underwater networks,” in _Proc. Of the First workshop on Underwater networks (WuWNet)_, 2006, pp. 17-24.
  1. R. Headrick and L. Freitag, “Growth of underwater communication technology in the us navy,” _IEEE Commun. Mag._, vol. 47, no. 1, pp. 80-82, 2009.
  1. M. E.-Kantarci, H. T. Mouftah, and S. Oktug, “A survey of architectures and localization techniques for underwater acoustic sensor networks,” _IEEE Commun. Surveys & Tutorials_, vol. 13, no. 3, pp. 487-502.
  1. Benthos, Telesonar Underwater Acoustic Modems, http://www.benthos.com/acoustic-telesonar-modem-product-comparison.asp
  1. LinkQuest, Soundlink Underwater Acoustic Modems, http://www.link-quest.com/html/intro1.htm
  1. DSPComm, AquaComm Underwater Wireless Modem, http://www.dspcomm.com/products_aquacomm.html
  1. L. Freita, M. Grund, S. Sing, J. Partan, P. Loski, and K. Ball, “The WHOI micro-modem: an acoustic communications and navigations system for multiple platforms,” in _Proc. IEEE/MTS OCEANS Conf. Exhib._, 2005, pp. 1086-1092. [http://acomms.whoi.edu/umodem/](http://acomms.whoi.edu/umodem/)
  1. E. Sozer and M. Stojanovic, “Reconfigurable acoustic modem for underwater sensor networks,” in _Proc. WUWNet_, 2006, pp. 101-104.
  1. B. Borowski and D. Duchamp, “Short Paper: the software modem – a software modem for underwater acoustic communication,” in _Proc. WUWNet_, 2009.
  1. N. B. Priyantha, B. Nissanka, A. Chakraborty, H. Balakrishnan, “The cricket location-suppot system,” in _Proc. of Mobile Computing and Networking (MobiCom)_, pp. 32-43, 2000.
  1. W. Bender, D. Gruhl, N. Morimoto, and A. Lu, “Techniques for data hiding,” _IBM Systems Journal_, vol. 35, nos. 3-4, 1996.
  1. V. Gerasimov and W. Bender, “Things that talk: using sound for device-to-device and device-to-human communication,” _IBM Systems Journal_, vol. 39, nos. 3-4, 2000.
  1. C. V. Lopes and P. M. Q. Aguiar, “Aerial acoustic communications,” in _IEEE Workshop on Applications of Signal Processing to Audio and Acoustics_, pp. 219-222, 2001.
  1. C. V. Lopes and P. M. Q. Aguiar, “Acoustic Modems for Ubiquitous Computing,” in _IEEE Pervasive Computing_, vol. 2, no. 3, pp. 62-71, 2003. http://www.ics.uci.edu/~lopes/dv/dv.html
  1. C. Peng, G. Shen, Y. Zhang, and K. Tan, “BeepBeep: a high accuracy acoustic ranging system using COTS mobile devices,” in _Proc. Embedded Sensor Network Systems (SenSys)_, pp. 1-14, 2007.
  1. C. Peng, G. Shen, Y. Zhang, and S. Lu, “Point&Connect: intention-based device pairing for mobile phone users,” in _Proc. Mobile Systems, Applications, and Services (MobiSys)_, pp. 137-149, 209.
  1. I. Constandache, X. Bao, M. Azizya, and R. R. Choudhury, “Did you see bob? human localization using mobile phones,” in _Proc. Mobile Computing and Networking (MobiCom)_, pp. 149-160, 2010.
  1. Y. Nishimura, N. Imai, and K. Yoshihara, “A proposal on direction estimation between devise using acoustic waves,” in _Proc. Mobile and Ubiquitous Systems (MobiQuitous)_, 2011.
  1. GitHub, Digital Voices repository, https://github.com/diva/digital-voices