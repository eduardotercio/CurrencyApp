# Currency Converter App

This app is a Compose Multiplatform application developed during a course. Its primary function is to convert currencies using data from an API, which provides exchange rates for different countries. Users can choose from over a hundred available currencies to convert. To minimize the number of requests, these currencies are also stored locally.

## Features

- **API Integration**: Fetches the latest exchange rates from a reliable currency API.
- **Currency Conversion**: Allows users to convert between over a hundred different currencies.
- **Local Persistence**: Stores currency data locally to reduce the number of API requests and enhance performance.

## Learning Experience

This project was a significant learning experience as it was my first venture into building a Compose Multiplatform app. I went beyond the course material by:

- **Custom Logic**: Modifying and creating custom logic to fit my personal coding style.
- **Additional Libraries**: Integrating additional libraries such as native ViewModel, Lifecycle, and Navigation, instead of using Voyager as taught in the course.
- **UseCases and Repositories**: Building use cases and repositories to manage network and local data operations.
- **MVI Architecture**: Implementing Model-View-Intent (MVI) architecture for better state management.
- **Conversion Trigger**: Removing the conversion button and integrate the logic into various actions, such as selecting a currency, changing the amount, or switching currencies.

## Technologies Used

- **Compose Multiplatform**: For building UI across different platforms.
- **Native ViewModel and Lifecycle**: For managing UI-related data in a lifecycle-conscious way.
- **Kotlin Coroutines**: For asynchronous programming.
- **Mongo Realm Database**: For local data persistence.

## Project Structure

- **API Layer**: Handles all API calls and network operations.
- **Database Layer**: Using Realm as database and also Settings Library as preferences.
- **Repository Layer**: Manages data operations, deciding whether to fetch data from the API or local database.
- **UseCases**: Encapsulates business logic for various features.
- **ViewModel**: Connects the UI with the business logic.
- **UI Layer**: Built using Jetpack Compose for a modern, declarative UI.

## Contact

Eduardo Tercio - [Linkedin](https://www.linkedin.com/in/eduardo-tercio/) - dudubezerra33@gmail.com
